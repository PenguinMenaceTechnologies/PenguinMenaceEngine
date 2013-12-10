package net.pme.math;

/**
 * A vector.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Vector3D {
	/**
	 * The x component.
	 */
	public double x;
	/**
	 * The y component.
	 */
	public double y;
	/**
	 * The z component.
	 */
	public double z;

	/**
	 * Create a null vector.
	 */
	public Vector3D() {
		this(0, 0, 0);
	}

	/**
	 * Create a vector with the given parameters.
	 * 
	 * @param x
	 *            x-component
	 * @param y
	 *            y-component
	 * @param z
	 *            z-component
	 */
	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Multiply a vector with a scalar.
	 * 
	 * @param v
	 *            The vector.
	 * @param scalar
	 *            The scalar.
	 * @return The scaled vector.
	 */
	public static Vector3D multiply(Vector3D v, double scalar) {
		Vector3D out = new Vector3D();
		out.x = v.x * scalar;
		out.y = v.y * scalar;
		out.z = v.z * scalar;
		return out;
	}

	/**
	 * Calculate the cross product of 2 vectors (vect1 x vect2)
	 * 
	 * @param vect1
	 *            The first vector.
	 * @param vect2
	 *            The second vector.
	 * @return The product.
	 */
	public static Vector3D crossProduct(Vector3D vect1, Vector3D vect2) {
		double t1 = vect1.y * vect2.z - vect1.z * vect2.y;
		double t2 = vect1.z * vect2.x - vect1.x * vect2.z;
		double t3 = vect1.x * vect2.y - vect1.y * vect2.x;
		return new Vector3D(t1, t2, t3);
	}

	/**
	 * Calculate the dot product of 2 vectors (vect1*vect2)
	 * 
	 * @param vect1
	 *            The first vector.
	 * @param vect2
	 *            The second vector.
	 * @return The product.
	 */
	public static double dotProduct(Vector3D vect1, Vector3D vect2) {
		return vect1.x * vect2.x + vect1.y * vect2.y + vect1.z * vect2.z;
	}

	/**
	 * Add 2 vectors.
	 * 
	 * @param vect1
	 *            The first vector.
	 * @param vect2
	 *            The second vector.
	 * @return The result.
	 */
	public static Vector3D add(Vector3D vect1, Vector3D vect2) {
		return new Vector3D(vect1.x + vect2.x, vect1.y + vect2.y, vect1.z
				+ vect2.z);
	}

	/**
	 * Normalize a given vector (v / length(v))
	 * 
	 * @param v
	 *            The vector to normalize.
	 * @return The normalized vector.
	 */
	public static Vector3D normalize(Vector3D v) {
		return multiply(v, 1.0 / length(v));
	}

	/**
	 * Calculate the length of a vector. (||v||)
	 * 
	 * @param v
	 *            The vector.
	 * @return The length of the vector.
	 */
	public static double length(Vector3D v) {
		return Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));
	}

	/**
	 * Transform a vector by a matrix.
	 * 
	 * @param v
	 *            The vector to transform.
	 * @param m
	 *            The transformation matrix.
	 * @return The transformed vector.
	 */
	public static Vector3D transformCoords(Vector3D v, Matrix m) {
		Vector3D vReuslt = new Vector3D(v.x * m.m11 + v.y * m.m21 + v.z * m.m31
				+ m.m41, v.x * m.m12 + v.y * m.m22 + v.z * m.m32 + m.m42, v.x
				* m.m13 + v.y * m.m23 + v.z * m.m33 + m.m43);
		double w = v.x * m.m14 + v.y * m.m24 + v.z * m.m34 + m.m44;
		if (w != 1.0f) {
			vReuslt = multiply(vReuslt, 1 / w);
		}

		return vReuslt;
	}

	/**
	 * Transform normals.
	 * 
	 * @param v
	 *            The normal to transform.
	 * @param m
	 *            The transformation matrix.
	 * @return The transformed normal.
	 */
	public static Vector3D transformNormal(Vector3D v, Matrix m) {
		double dLength = length(v);
		if (dLength == 0.0)
			return v;
		Vector3D vReuslt = new Vector3D(v.x * m.m11 + v.y * m.m21 + v.z * m.m31
				+ m.m41, v.x * m.m12 + v.y * m.m22 + v.z * m.m32 + m.m42, v.x
				* m.m13 + v.y * m.m23 + v.z * m.m33 + m.m43);
		double w = v.x * m.m14 + v.y * m.m24 + v.z * m.m34 + m.m44;
		if (w != 1.0f) {
			vReuslt = multiply(vReuslt, 1 / w);
		}

		return vReuslt;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Vector3D) {
			Vector3D other = (Vector3D) object;

			if (Math.abs(other.x - this.x) < 10E-9
					&& Math.abs(other.y - this.y) < 10E-9
					&& Math.abs(other.z - this.z) < 10E-9) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}

	public double[] toArray() {
		double[] res = new double[3];
		res[0] = x;
		res[1] = y;
		res[2] = z;
		return res;
	}
}
