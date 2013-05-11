package net.pme.math;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

/**
 * Matrix operations.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Matrix {
	double m11, m12, m13, m14, m21, m22, m23, m24, m31, m32, m33, m34, m41,
			m42, m43, m44;

	/**
	 * Creates a MatrixIdentity
	 */
	public Matrix() {
		this(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
				0.0, 0.0, 1.0);
	}

	/**
	 * Creates a matrix with the specified parameters mab (a is the row, b the
	 * column)
	 * 
	 * @param m11
	 * @param m12
	 * @param m13
	 * @param m14
	 * @param m21
	 * @param m22
	 * @param m23
	 * @param m24
	 * @param m31
	 * @param m32
	 * @param m33
	 * @param m34
	 * @param m41
	 * @param m42
	 * @param m43
	 * @param m44
	 */
	public Matrix(double m11, double m12, double m13, double m14, double m21,
			double m22, double m23, double m24, double m31, double m32,
			double m33, double m34, double m41, double m42, double m43,
			double m44) {
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m14 = m14;

		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m24 = m24;

		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
		this.m34 = m34;

		this.m41 = m41;
		this.m42 = m42;
		this.m43 = m43;
		this.m44 = m44;
	}

	/**
	 * Convert the Matrix into a DoubleBuffer.
	 * 
	 * @return A DoubleBuffer containing the matrix.
	 */
	public DoubleBuffer getValues() {
		double[] m = new double[4 * 4];
		m[0] = m11;
		m[1] = m12;
		m[2] = m13;
		m[3] = m14;
		m[4] = m21;
		m[5] = m22;
		m[6] = m23;
		m[7] = m24;
		m[8] = m31;
		m[9] = m32;
		m[10] = m33;
		m[11] = m34;
		m[12] = m41;
		m[13] = m42;
		m[14] = m43;
		m[15] = m44;
		DoubleBuffer db = BufferUtils.createDoubleBuffer(16);
		db.put(m);
		return db;
	}

	/**
	 * Return an identity Matrix.
	 * 
	 * @return The identity.
	 */
	public static Matrix identity() {
		return new Matrix();
	}

	/**
	 * Create a translation matrix from a vector.
	 * 
	 * @param v
	 *            The translation Vector.
	 * @return The translation matrix.
	 */
	public static Matrix translation(Vector3D v) {
		return new Matrix(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0,
				1.0, 0.0, v.x, v.y, v.z, 1.0);
	}

	/**
	 * Not implemented yet!
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Matrix rotation(double x, double y, double z) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	/**
	 * Rotate around the given vector.
	 * 
	 * @param v
	 *            The vector to rotate around.
	 * @param d
	 *            The angle (WARNING: not in degree)
	 * @return The rotation matrix.
	 */
	public static Matrix rotationAxis(Vector3D v, double d) {
		double dSin = Math.sin(-d);
		double dCos = Math.cos(-d);
		double dOneMinusCos = 1.0 - dCos;

		Vector3D vAxis = Vector3D.normalize(v);

		return new Matrix((vAxis.x * vAxis.x) * dOneMinusCos + dCos,
				(vAxis.x * vAxis.y) * dOneMinusCos - (vAxis.z * dSin),
				(vAxis.x * vAxis.z) * dOneMinusCos + (vAxis.y * dSin), 0.0d,
				(vAxis.y * vAxis.x) * dOneMinusCos + (vAxis.z * dSin),
				(vAxis.y * vAxis.y) * dOneMinusCos + dCos, (vAxis.y * vAxis.z)
						* dOneMinusCos - (vAxis.x * dSin), 0.0d,
				(vAxis.z * vAxis.x) * dOneMinusCos - (vAxis.y * dSin),
				(vAxis.z * vAxis.y) * dOneMinusCos + (vAxis.x * dSin),
				(vAxis.z * vAxis.z) * dOneMinusCos + dCos, 0.0d, 0.0d, 0.0d,
				0.0d, 1.0d);
	}

	/**
	 * A Matrix to scale. (Identity*Vector)
	 * 
	 * @param v
	 *            The vector to scale with.
	 * @return The scale matrix.
	 */
	public static Matrix scaling(Vector3D v) {
		return new Matrix(v.x, 0.0, 0.0, 0.0, 0.0, v.y, 0.0, 0.0, 0.0, 0.0,
				v.z, 0.0, 0.0, 0.0, 0.0, 1.0);
	}

	/**
	 * This matrix can adjust an object along the 3 given axes.
	 * 
	 * @param xAxis
	 * @param yAxis
	 * @param zAxis
	 * @return The axe matrix.
	 */
	public static Matrix axes(Vector3D xAxis, Vector3D yAxis, Vector3D zAxis) {
		return new Matrix(xAxis.x, xAxis.y, xAxis.z, 0.0, yAxis.x, yAxis.y,
				yAxis.z, 0.0, zAxis.x, zAxis.y, zAxis.z, 0.0, 0.0, 0.0, 0.0,
				1.0);
	}

	/**
	 * Calculate the determinant of the matrix.
	 * 
	 * @param m
	 *            Matrix to calculate the determinant from.
	 * @return The determinant.
	 */
	public static double det(Matrix m) {
		return m.m11 * (m.m22 * m.m33 - m.m23 * m.m32) - m.m12
				* (m.m21 * m.m33 - m.m23 * m.m31) + m.m13
				* (m.m21 * m.m32 - m.m22 * m.m31);
	}

	/**
	 * Calculate the inverse of a Matrix. (Asume the inverse exists)
	 * 
	 * @param m
	 *            The matrix to invert.
	 * @return The inverse of m.
	 */
	public static Matrix invert(Matrix m) {
		double dInvDet = det(m);
		if (dInvDet == 0.0)
			return identity();
		dInvDet = 1.0 / dInvDet;

		Matrix mResult = new Matrix();

		mResult.m11 = dInvDet * (m.m22 * m.m33 - m.m23 * m.m32);
		mResult.m12 = -dInvDet * (m.m12 * m.m33 - m.m13 * m.m32);
		mResult.m13 = dInvDet * (m.m12 * m.m23 - m.m13 * m.m22);
		mResult.m14 = 0.0;
		mResult.m21 = -dInvDet * (m.m21 * m.m33 - m.m23 * m.m31);
		mResult.m22 = dInvDet * (m.m11 * m.m33 - m.m13 * m.m31);
		mResult.m23 = -dInvDet * (m.m11 * m.m23 - m.m13 * m.m21);
		mResult.m24 = 0.0;
		mResult.m31 = dInvDet * (m.m21 * m.m32 - m.m22 * m.m31);
		mResult.m32 = -dInvDet * (m.m11 * m.m32 - m.m12 * m.m31);
		mResult.m33 = dInvDet * (m.m11 * m.m22 - m.m12 * m.m21);
		mResult.m34 = 0.0;
		mResult.m41 = -(m.m41 * mResult.m11 + m.m42 * mResult.m21 + m.m43
				* mResult.m31);
		mResult.m42 = -(m.m41 * mResult.m12 + m.m42 * mResult.m22 + m.m43
				* mResult.m32);
		mResult.m43 = -(m.m41 * mResult.m13 + m.m42 * mResult.m23 + m.m43
				* mResult.m33);
		mResult.m44 = 1.0;

		return mResult;
	}

	/**
	 * Transpose a matrix.
	 * 
	 * @param m
	 *            The matrix to transpose.
	 * @return The transposed matrix.
	 */
	public static Matrix transpose(Matrix m) {
		return new Matrix(m.m11, m.m21, m.m31, m.m41, m.m12, m.m22, m.m32,
				m.m42, m.m13, m.m23, m.m33, m.m43, m.m14, m.m24, m.m34, m.m44);
	}

	/**
	 * The euler rotation around the x-Axis
	 * 
	 * @return The euler rotation around the x-Axis
	 */
	public double xRotation() {
		return Math.atan2(m32, m33);
	}

	/**
	 * The euler rotation around the y-Axis
	 * 
	 * @return The euler rotation around the y-Axis
	 */
	public double yRotation() {
		double asin = Math.PI - Math.asin(m31);

		if (m33 >= 0) {
			return -asin;
		}

		return asin;
	}

	/**
	 * The euler rotation around the z-Axis
	 * 
	 * @return The euler rotation around the z-Axis
	 */
	public double zRotation() {
		return Math.PI / 2 - Math.atan2(m21, m11);
	}

	/**
	 * Get a camera matrix from the given vetors.
	 * 
	 * @param position
	 *            Position of the camera.
	 * @param xAxis
	 *            xAxis of the camera.
	 * @param yAxis
	 *            yAxis of the camera.
	 * @param zAxis
	 *            zAxis of the camera.
	 * @return The camera matrix.
	 */
	public static Matrix camera(Vector3D position, Vector3D xAxis,
			Vector3D yAxis, Vector3D zAxis) {
		return Matrix.multiply(
				Matrix.translation(Vector3D.multiply(position, -1)),
				Matrix.transpose(Matrix.axes(xAxis, yAxis, zAxis)));
	}

	/**
	 * Multiply 2 matrices. (a*b)
	 * 
	 * @param a
	 *            Matrix a.
	 * @param b
	 *            Matrix b.
	 * @return The product.
	 */
	public static Matrix multiply(Matrix a, Matrix b) {
		return new Matrix(b.m11 * a.m11 + b.m21 * a.m12 + b.m31 * a.m13 + b.m41
				* a.m14, b.m12 * a.m11 + b.m22 * a.m12 + b.m32 * a.m13 + b.m42
				* a.m14, b.m13 * a.m11 + b.m23 * a.m12 + b.m33 * a.m13 + b.m43
				* a.m14, b.m14 * a.m11 + b.m24 * a.m12 + b.m34 * a.m13 + b.m44
				* a.m14, b.m11 * a.m21 + b.m21 * a.m22 + b.m31 * a.m23 + b.m41
				* a.m24, b.m12 * a.m21 + b.m22 * a.m22 + b.m32 * a.m23 + b.m42
				* a.m24, b.m13 * a.m21 + b.m23 * a.m22 + b.m33 * a.m23 + b.m43
				* a.m24, b.m14 * a.m21 + b.m24 * a.m22 + b.m34 * a.m23 + b.m44
				* a.m24, b.m11 * a.m31 + b.m21 * a.m32 + b.m31 * a.m33 + b.m41
				* a.m34, b.m12 * a.m31 + b.m22 * a.m32 + b.m32 * a.m33 + b.m42
				* a.m34, b.m13 * a.m31 + b.m23 * a.m32 + b.m33 * a.m33 + b.m43
				* a.m34, b.m14 * a.m31 + b.m24 * a.m32 + b.m34 * a.m33 + b.m44
				* a.m34, b.m11 * a.m41 + b.m21 * a.m42 + b.m31 * a.m43 + b.m41
				* a.m44, b.m12 * a.m41 + b.m22 * a.m42 + b.m32 * a.m43 + b.m42
				* a.m44, b.m13 * a.m41 + b.m23 * a.m42 + b.m33 * a.m43 + b.m43
				* a.m44, b.m14 * a.m41 + b.m24 * a.m42 + b.m34 * a.m43 + b.m44
				* a.m44);
	}
}
