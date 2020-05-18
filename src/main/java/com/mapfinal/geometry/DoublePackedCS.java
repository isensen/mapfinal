package com.mapfinal.geometry;

import java.util.Arrays;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.CoordinateXYM;
import org.locationtech.jts.geom.CoordinateXYZM;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.impl.PackedCoordinateSequence;

public class DoublePackedCS extends PackedCoordinateSequence {
	private static final long serialVersionUID = 5625667336768794207L;
	/**
	 * The packed coordinate array
	 */
	double[] coords;

	/**
	 * Builds a new packed coordinate sequence
	 *
	 * @param coords
	 * @param dimension
	 * @param measures
	 */
	public DoublePackedCS(double[] coords, int dimension, int measures) {
		super(dimension, measures);
		if (coords.length % dimension != 0) {
			throw new IllegalArgumentException("Packed array does not contain " + "an integral number of coordinates");
		}
		this.coords = coords;
	}

	/**
	 * Builds a new packed coordinate sequence out of a float coordinate array
	 *
	 * @param coordinates
	 * @param dimensions
	 * @param measures
	 */
	public DoublePackedCS(float[] coordinates, int dimension, int measures) {
		super(dimension, measures);
		this.coords = new double[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) {
			this.coords[i] = coordinates[i];
		}
	}

	/**
	 * Builds a new packed coordinate sequence out of a coordinate array
	 * 
	 * @param coordinates
	 * @param dimension
	 */
	public DoublePackedCS(Coordinate[] coordinates, int dimension) {
		this(coordinates, dimension, 0);
	}

	/**
	 * Builds a new packed coordinate sequence out of a coordinate array
	 *
	 * @param coordinates
	 * @param dimensions
	 * @param measures
	 */
	public DoublePackedCS(Coordinate[] coordinates, int dimension, int measures) {
		super(dimension, measures);
		if (coordinates == null)
			coordinates = new Coordinate[0];

		coords = new double[coordinates.length * this.dimension];
		for (int i = 0; i < coordinates.length; i++) {
			coords[i * this.dimension] = coordinates[i].x;
			if (this.dimension >= 2)
				coords[i * this.dimension + 1] = coordinates[i].y;
			if (this.dimension >= 3)
				coords[i * this.dimension + 2] = coordinates[i].getOrdinate(2); // Z or M
			if (this.dimension >= 4)
				coords[i * this.dimension + 3] = coordinates[i].getOrdinate(3); // M
		}
	}

	/**
	 * Builds a new packed coordinate sequence out of a coordinate array
	 *
	 * @param coordinates
	 */
	public DoublePackedCS(Coordinate[] coordinates) {
		this(coordinates, 3, 0);
	}

	/**
	 * Builds a new empty packed coordinate sequence of a given size and dimension
	 */
	public DoublePackedCS(int size, int dimension, int measures) {
		super(dimension, measures);
		coords = new double[size * this.dimension];
	}

	/**
	 * @see org.locationtech.jts.geom.CoordinateSequence#getCoordinate(int)
	 */
	public Coordinate getCoordinateInternal(int i) {
		double x = coords[i * dimension];
		double y = coords[i * dimension + 1];
		if (dimension == 2 && measures == 0) {
			return new CoordinateXY(x, y);
		} else if (dimension == 3 && measures == 0) {
			double z = coords[i * dimension + 2];
			return new Coordinate(x, y, z);
		} else if (dimension == 3 && measures == 1) {
			double m = coords[i * dimension + 2];
			return new CoordinateXYM(x, y, m);
		} else if (dimension == 4 && measures == 1) {
			double z = coords[i * dimension + 2];
			double m = coords[i * dimension + 3];
			return new CoordinateXYZM(x, y, z, m);
		}
		return new Coordinate(x, y);
	}

	/**
	 * Gets the underlying array containing the coordinate values.
	 * 
	 * @return the array of coordinate values
	 */
	public double[] getRawCoordinates() {
		return coords;
	}

	/**
	 * @see org.locationtech.jts.geom.CoordinateSequence#size()
	 */
	public int size() {
		return coords.length / dimension;
	}

	/**
	 * @see java.lang.Object#clone()
	 * @deprecated
	 */
	public Object clone() {
		return copy();
	}

	public Double copy() {
		double[] clone = Arrays.copyOf(coords, coords.length);
		return new Double(clone, dimension, measures);
	}

	/**
	 * @see org.locationtech.jts.geom.CoordinateSequence#getOrdinate(int, int)
	 *      Beware, for performance reasons the ordinate index is not checked, if
	 *      it's over dimensions you may not get an exception but a meaningless
	 *      value.
	 */
	public double getOrdinate(int index, int ordinate) {
		return coords[index * dimension + ordinate];
	}

	/**
	 * @see com.vividsolutions.jts.geom.PackedCoordinateSequence#setOrdinate(int,
	 *      int, double)
	 */
	public void setOrdinate(int index, int ordinate, double value) {
		coordRef = null;
		coords[index * dimension + ordinate] = value;
	}

	public Envelope expandEnvelope(Envelope env) {
		for (int i = 0; i < coords.length; i += dimension) {
			env.expandToInclude(coords[i], coords[i + 1]);
		}
		return env;
	}

}
