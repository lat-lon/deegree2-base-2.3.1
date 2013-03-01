//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/tools/srs/TransformShapeFile.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.tools.srs;

import java.io.File;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;

import org.deegree.framework.util.FileUtils;
import org.deegree.framework.util.Pair;
import org.deegree.io.shpapi.shape_new.Shape;
import org.deegree.io.shpapi.shape_new.ShapeEnvelope;
import org.deegree.io.shpapi.shape_new.ShapeFile;
import org.deegree.io.shpapi.shape_new.ShapeFileReader;
import org.deegree.io.shpapi.shape_new.ShapeFileWriter;
import org.deegree.model.crs.CRSFactory;
import org.deegree.model.crs.CRSTransformationException;
import org.deegree.model.crs.GeoTransformer;
import org.deegree.model.crs.UnknownCRSException;
import org.deegree.model.feature.Feature;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Point;

/**
 * Tool to transform shapefiles from one CRS to another.
 * 
 * @author <a href="mailto:tonnhofer@lat-lon.de">Oliver Tonnhofer</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 14987 $, $Date: 2008-11-20 17:06:18 +0100 (Do, 20. Nov
 *          2008) $
 */
public class TransformShapeFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length % 2 != 0)
			printHelpAndExit();

		Properties map = new Properties();
		for (int i = 0; i < args.length; i += 2) {
			map.put(args[i], args[i + 1]);
		}

		if ((String) map.get("-transformId") != null) {
			handleNtv2Option(map);
		} else {
			handleCrsOption(map);
		}

	}

	private static void printHelpAndExit() {
		System.out
				.println("Usage: java [...] org.deegree.tools.srs.TransformShapeFile ");
		System.out
				.println("                  -inFile shapeBasename -inCRS crs ");
		System.out
				.println("                  [-outFile shapeBasename] -outCRS crs");
		System.exit(1);
	}

	private static void handleNtv2Option(final Properties map) {
		String transformId = (String) map.get("-transformId");
		if (transformId == null) {
			printHelpAndExit();
		}

		String inFilename = determineInFileName(map);
		String outFilename = determineOutFileName(map, inFilename, transformId);

		try {
			transformShapeFileWithNtv2(transformId, inFilename, outFilename);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.exit(2);
		}
	}

	private static void handleCrsOption(final Properties map) {
		String outCRS = (String) map.get("-outCRS");
		if (outCRS == null)
			printHelpAndExit();

		String inCRS = (String) map.get("-inCRS");
		if (inCRS == null)
			printHelpAndExit();

		String inFilename = determineInFileName(map);
		String outFilename = determineOutFileName(map, inFilename, outCRS);

		try {
			transformShapeFile(inFilename, inCRS, outFilename, outCRS);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.exit(2);
		}
	}

	private static String determineInFileName(final Properties map) {
		String inFilename = (String) map.get("-inFile");
		if (inFilename == null)
			printHelpAndExit();
		return inFilename;
	}

	private static String determineOutFileName(final Properties map,
			String inFilename, String postfix) {
		String outFilename = (String) map.get("-outFile");
		if (outFilename == null) {
			outFilename = inFilename + "." + postfix;
		}
		return outFilename;
	}
	
	private static void transformShapeFile(String inFile, String inCRS,
			String outFile, String outCRS) throws Exception {

		final ShapeFileReader reader = new ShapeFileReader(inFile,
				CRSFactory.create(inCRS));
		final int type = reader.getShapeType();

		System.out.println("Transforming:                  ");

		int cnt = reader.getShapeCount();
		int step = (int) Math.floor(5 * (cnt * 0.01));
		step = Math.max(1, step);

		final GeoTransformer gt = new GeoTransformer(outCRS);
		final ShapeEnvelope envelope = reader.getEnvelope();

		determineEnvelope(inCRS, gt, envelope);

		outFile = cutShpPostfix(outFile);
		inFile = cutShpPostfix(inFile);

		ShapeFileWriter writer = new ShapeFileWriter(outFile);
		Pair<OutputStream, OutputStream> streamPair = writer.writeHeaders(
				(int) new File(inFile).length(), cnt, type, envelope);

		int i = 0;
		int percentage = 0;
		Iterator<Feature> iter = reader.iterator();
		while (iter.hasNext()) {
			Feature feature = iter.next();
			feature = gt.transform(feature);
			Shape shape = ShapeFile.extractShape(feature);
			writer.writeShape(streamPair.first, streamPair.second, shape);
			if (i++ % step == 0) {
				System.out.print("\r" + (percentage)
						+ (((percentage) < 10) ? "  " : " ") + "% transformed");
				percentage += 5;
			}
		}
		System.out.println();

		streamPair.first.close();
		streamPair.second.close();
		FileUtils.copy(new File(inFile + ".dbf"), new File(outFile + ".dbf"));
	}

	private static String cutShpPostfix(String outFile) {
		if (outFile.toLowerCase().endsWith(".shp")) {
			outFile = outFile.substring(0, outFile.length() - 4);
		}
		return outFile;
	}

	private static void determineEnvelope(String inCRS,
			final GeoTransformer gt, final ShapeEnvelope envelope)
			throws UnknownCRSException, CRSTransformationException {
		final Point ptMin = GeometryFactory.createPoint(envelope.xmin,
				envelope.ymin, CRSFactory.create(inCRS));
		final Point ptMinTransformed = (Point) gt.transform(ptMin);
		envelope.xmin = ptMinTransformed.getX();
		envelope.ymin = ptMinTransformed.getY();

		final Point ptMax = GeometryFactory.createPoint(envelope.xmax,
				envelope.ymax, CRSFactory.create(inCRS));
		final Point ptMaxTransformed = (Point) gt.transform(ptMax);
		envelope.xmax = ptMaxTransformed.getX();
		envelope.ymax = ptMaxTransformed.getY();
	}

	private static void transformShapeFileWithNtv2(String ntv2Id,
			String inFile, String outFile) throws Exception {
		System.out.println("Attemting to do a ntv2 transformation with id: "
				+ ntv2Id);
		throw new UnsupportedOperationException("Not yet implemented");
		// TODO
	}
}
