package net.pme.core.math;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

/**
 * Matrix operations.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class Matrix {
    /**
     * The dimension of the array.
     */
    private static final int N = 4;
    private static final double ZERO = 0.0;
    private double[][] m;

    /**
     * Creates a MatrixIdentity.
     */
    public Matrix() {
        this(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, 1.0);
    }

    /**
     * Create a matrix from a lwjgl Matrix4f.
     * @param matrix4f The input matrix.
     */
    public Matrix(Matrix4f matrix4f) {
        this(matrix4f.m00, matrix4f.m01, matrix4f.m02, matrix4f.m03,
                matrix4f.m10, matrix4f.m11, matrix4f.m12, matrix4f.m13,
                matrix4f.m20, matrix4f.m21, matrix4f.m22, matrix4f.m23,
                matrix4f.m30, matrix4f.m31, matrix4f.m32, matrix4f.m33);
    }

    /**
     * Creates a matrix with the specified parameters mab (a is the row, b the
     * column).
     *
     * @param initialValues The initial values of the matrix, ordered by i and then j with
     *                      m[i][j]. The amount of parameters must be 16.
     */
    public Matrix(final double... initialValues) {
        if (initialValues.length != N * N) {
            throw new IllegalArgumentException("Argument count must be 16");
        }
        m = new double[N][N];
        int i = 0;
        for (int x = 0; x < m.length; x++) {
            for (int y = 0; y < m[x].length; y++) {
                m[x][y] = initialValues[i++];
            }
        }
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
     * @param v The translation Vector.
     * @return The translation matrix.
     */
    public static Matrix translation(final Vector3d v) {
        return new Matrix(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0,
                1.0, 0.0, v.getX(), v.getY(), v.getZ(), 1.0);
    }

    /**
     * Create a rotation matrix for the euler-angles.
     * <p/>
     * Not implemented yet.
     *
     * @param x The x-eulers-angle.
     * @param y The y-eulers-angle.
     * @param z The z-eulers-angle.
     * @return The rotation matrix.
     */
    public static Matrix rotation(final double x, final double y, final double z) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Rotate around the given vector.
     *
     * @param v The vector to rotate around.
     * @param d The angle (in radian)
     * @return The rotation matrix.
     */
    public static Matrix rotationAxis(final Vector3d v, final double d) {
        double dSin = Math.sin(-d);
        double dCos = Math.cos(-d);
        double dOneMinusCos = 1.0 - dCos;

        Vector3d vAxis = v.normalize();

        return new Matrix((vAxis.getX() * vAxis.getX()) * dOneMinusCos + dCos,
                (vAxis.getX() * vAxis.getY()) * dOneMinusCos - (vAxis.getZ() * dSin),
                (vAxis.getX() * vAxis.getZ()) * dOneMinusCos + (vAxis.getY() * dSin), 0.0d,
                (vAxis.getY() * vAxis.getX()) * dOneMinusCos + (vAxis.getZ() * dSin),
                (vAxis.getY() * vAxis.getY()) * dOneMinusCos + dCos, (vAxis.getY() * vAxis.getZ())
                * dOneMinusCos - (vAxis.getX() * dSin), 0.0d,
                (vAxis.getZ() * vAxis.getX()) * dOneMinusCos - (vAxis.getY() * dSin),
                (vAxis.getZ() * vAxis.getY()) * dOneMinusCos + (vAxis.getX() * dSin),
                (vAxis.getZ() * vAxis.getZ()) * dOneMinusCos + dCos, 0.0d, 0.0d, 0.0d,
                0.0d, 1.0d
        );
    }

    /**
     * A Matrix to scale. (Identity*Vector)
     *
     * @param v The vector to scale with.
     * @return The scale matrix.
     */
    public static Matrix scaling(final Vector3d v) {
        return new Matrix(v.getX(), 0.0, 0.0, 0.0, 0.0, v.getY(), 0.0, 0.0, 0.0, 0.0,
                v.getZ(), 0.0, 0.0, 0.0, 0.0, 1.0);
    }

    /**
     * This matrix can adjust an object along the 3 given axes.
     *
     * @param xAxis The x axis to adjust to.
     * @param yAxis The y axis to adjust to.
     * @param zAxis The z axis to adjust to.
     * @return The axe matrix.
     */
    public static Matrix axes(final Vector3d xAxis, final Vector3d yAxis, final Vector3d zAxis) {
        return new Matrix(xAxis.getX(), xAxis.getY(), xAxis.getZ(), 0.0, yAxis.getX(), yAxis.getY(),
                yAxis.getZ(), 0.0, zAxis.getX(), zAxis.getY(), zAxis.getZ(), 0.0, 0.0, 0.0, 0.0,
                1.0);
    }

    /**
     * Get a camera matrix from the given vetors.
     *
     * @param position Position of the camera.
     * @param xAxis    xAxis of the camera.
     * @param yAxis    yAxis of the camera.
     * @param zAxis    zAxis of the camera.
     * @return The camera matrix.
     */
    public static Matrix camera(final Vector3d position, final Vector3d xAxis,
                                final Vector3d yAxis, final Vector3d zAxis) {
        return Matrix.translation(position.clone().scale(-1)).multiply(
                Matrix.axes(xAxis, yAxis, zAxis).transpose());
    }

    /**
     * Creates a matrix with the specified parameters mab (a is the row, b the
     * column).
     *
     * @param initialValues The initial values of the matrix, ordered by i and then j with
     *                      m[i][j]. The amount of parameters must be 16.
     */
    public Matrix set(final double... initialValues) {
        if (initialValues.length != N * N) {
            throw new IllegalArgumentException("Argument count must be 16");
        }
        int i = 0;
        for (int x = 0; x < m.length; x++) {
            for (int y = 0; y < m[x].length; y++) {
                m[x][y] = initialValues[i++];
            }
        }
        return this;
    }

    /**
     * Calculate the determinant of the matrix.
     *
     * @return The determinant.
     */
    public double det() {
        Matrix m = this;
        return m.m[0][0] * (m.m[1][1] * m.m[2][2] - m.m[1][2] * m.m[2][1])
                - m.m[0][1] * (m.m[1][0] * m.m[2][2] - m.m[1][2] * m.m[2][0])
                + m.m[0][2] * (m.m[1][0] * m.m[2][1] - m.m[1][1] * m.m[2][0]);
    }

    /**
     * Calculate the inverse of a Matrix. (Asume the inverse exists)
     *
     * @return The inverse of m.
     */
    public Matrix invert() {
        Matrix m = this;
        double dInvDet = det();
        if (dInvDet == 0.0) {
            return identity();
        }
        dInvDet = 1.0 / dInvDet;

        Matrix mResult = new Matrix();

        mResult.m[0][0] = dInvDet
                * (m.m[1][1] * m.m[2][2] - m.m[1][2] * m.m[2][1]);
        mResult.m[0][1] = -dInvDet
                * (m.m[0][1] * m.m[2][2] - m.m[0][2] * m.m[2][1]);
        mResult.m[0][2] = dInvDet
                * (m.m[0][1] * m.m[1][2] - m.m[0][2] * m.m[1][1]);
        mResult.m[0][3] = ZERO;
        mResult.m[1][0] = -dInvDet
                * (m.m[1][0] * m.m[2][2] - m.m[1][2] * m.m[2][0]);
        mResult.m[1][1] = dInvDet
                * (m.m[0][0] * m.m[2][2] - m.m[0][2] * m.m[2][0]);
        mResult.m[1][2] = -dInvDet
                * (m.m[0][0] * m.m[1][2] - m.m[0][2] * m.m[1][0]);
        mResult.m[1][3] = ZERO;
        mResult.m[2][0] = dInvDet
                * (m.m[1][0] * m.m[2][1] - m.m[1][1] * m.m[2][0]);
        mResult.m[2][1] = -dInvDet
                * (m.m[0][0] * m.m[2][1] - m.m[0][1] * m.m[2][0]);
        mResult.m[2][2] = dInvDet
                * (m.m[0][0] * m.m[1][1] - m.m[0][1] * m.m[1][0]);
        mResult.m[2][3] = ZERO;
        mResult.m[3][0] = -(m.m[3][0] * mResult.m[0][0] + m.m[3][1]
                * mResult.m[1][0] + m.m[3][2] * mResult.m[2][0]);
        mResult.m[3][1] = -(m.m[3][0] * mResult.m[0][1] + m.m[3][1]
                * mResult.m[1][1] + m.m[3][2] * mResult.m[2][1]);
        mResult.m[3][2] = -(m.m[3][0] * mResult.m[0][2] + m.m[3][1]
                * mResult.m[1][2] + m.m[3][2] * mResult.m[2][2]);
        mResult.m[3][3] = 1.0;

        return set(mResult);
    }

    /**
     * Set the values of this matrix to the other matrix.
     * @param other The matrix to which the values of this one should be set.
     * @return The matrix to continue calculations.
     */
    private Matrix set(final Matrix other) {
        for (int i = 0; i < m.length; i++) {
            System.arraycopy(other.m[i], 0, m[i], 0, m[i].length);
        }
        return this;
    }

    /**
     * Transpose a matrix.
     *
     * @return The transposed matrix.
     */
    public Matrix transpose() {
        Matrix m = this;
        return set(m.m[0][0], m.m[1][0], m.m[2][0], m.m[3][0],
                m.m[0][1], m.m[1][1], m.m[2][1], m.m[3][1], m.m[0][2],
                m.m[1][2], m.m[2][2], m.m[3][2], m.m[0][3], m.m[1][3],
                m.m[2][3], m.m[3][3]);
    }

    /**
     * Multiply 2 matrices. (this * b)
     *
     * @param b Matrix b.
     * @return The product.
     */
    public Matrix multiply(final Matrix b) {
        Matrix a = this;
        return set(b.m[0][0] * a.m[0][0] + b.m[1][0] * a.m[0][1]
                + b.m[2][0] * a.m[0][2] + b.m[3][0] * a.m[0][3], b.m[0][1]
                * a.m[0][0] + b.m[1][1] * a.m[0][1] + b.m[2][1] * a.m[0][2]
                + b.m[3][1] * a.m[0][3], b.m[0][2] * a.m[0][0] + b.m[1][2]
                * a.m[0][1] + b.m[2][2] * a.m[0][2] + b.m[3][2] * a.m[0][3],
                b.m[0][3] * a.m[0][0] + b.m[1][3] * a.m[0][1] + b.m[2][3]
                        * a.m[0][2] + b.m[3][3] * a.m[0][3], b.m[0][0]
                * a.m[1][0] + b.m[1][0] * a.m[1][1] + b.m[2][0]
                * a.m[1][2] + b.m[3][0] * a.m[1][3], b.m[0][1]
                * a.m[1][0] + b.m[1][1] * a.m[1][1] + b.m[2][1]
                * a.m[1][2] + b.m[3][1] * a.m[1][3], b.m[0][2]
                * a.m[1][0] + b.m[1][2] * a.m[1][1] + b.m[2][2]
                * a.m[1][2] + b.m[3][2] * a.m[1][3], b.m[0][3]
                * a.m[1][0] + b.m[1][3] * a.m[1][1] + b.m[2][3]
                * a.m[1][2] + b.m[3][3] * a.m[1][3], b.m[0][0]
                * a.m[2][0] + b.m[1][0] * a.m[2][1] + b.m[2][0]
                * a.m[2][2] + b.m[3][0] * a.m[2][3], b.m[0][1]
                * a.m[2][0] + b.m[1][1] * a.m[2][1] + b.m[2][1]
                * a.m[2][2] + b.m[3][1] * a.m[2][3], b.m[0][2]
                * a.m[2][0] + b.m[1][2] * a.m[2][1] + b.m[2][2]
                * a.m[2][2] + b.m[3][2] * a.m[2][3], b.m[0][3]
                * a.m[2][0] + b.m[1][3] * a.m[2][1] + b.m[2][3]
                * a.m[2][2] + b.m[3][3] * a.m[2][3], b.m[0][0]
                * a.m[3][0] + b.m[1][0] * a.m[3][1] + b.m[2][0]
                * a.m[3][2] + b.m[3][0] * a.m[3][3], b.m[0][1]
                * a.m[3][0] + b.m[1][1] * a.m[3][1] + b.m[2][1]
                * a.m[3][2] + b.m[3][1] * a.m[3][3], b.m[0][2]
                * a.m[3][0] + b.m[1][2] * a.m[3][1] + b.m[2][2]
                * a.m[3][2] + b.m[3][2] * a.m[3][3], b.m[0][3]
                * a.m[3][0] + b.m[1][3] * a.m[3][1] + b.m[2][3]
                * a.m[3][2] + b.m[3][3] * a.m[3][3]
        );
    }

    /**
     * Multiply 2 matrices. (a * this)
     *
     * @param a Matrix a.
     * @return The product.
     */
    public Matrix multiplyLeft(final Matrix a) {
        Matrix b = this;
        return set(b.m[0][0] * a.m[0][0] + b.m[1][0] * a.m[0][1]
                + b.m[2][0] * a.m[0][2] + b.m[3][0] * a.m[0][3], b.m[0][1]
                * a.m[0][0] + b.m[1][1] * a.m[0][1] + b.m[2][1] * a.m[0][2]
                + b.m[3][1] * a.m[0][3], b.m[0][2] * a.m[0][0] + b.m[1][2]
                * a.m[0][1] + b.m[2][2] * a.m[0][2] + b.m[3][2] * a.m[0][3],
                b.m[0][3] * a.m[0][0] + b.m[1][3] * a.m[0][1] + b.m[2][3]
                        * a.m[0][2] + b.m[3][3] * a.m[0][3], b.m[0][0]
                * a.m[1][0] + b.m[1][0] * a.m[1][1] + b.m[2][0]
                * a.m[1][2] + b.m[3][0] * a.m[1][3], b.m[0][1]
                * a.m[1][0] + b.m[1][1] * a.m[1][1] + b.m[2][1]
                * a.m[1][2] + b.m[3][1] * a.m[1][3], b.m[0][2]
                * a.m[1][0] + b.m[1][2] * a.m[1][1] + b.m[2][2]
                * a.m[1][2] + b.m[3][2] * a.m[1][3], b.m[0][3]
                * a.m[1][0] + b.m[1][3] * a.m[1][1] + b.m[2][3]
                * a.m[1][2] + b.m[3][3] * a.m[1][3], b.m[0][0]
                * a.m[2][0] + b.m[1][0] * a.m[2][1] + b.m[2][0]
                * a.m[2][2] + b.m[3][0] * a.m[2][3], b.m[0][1]
                * a.m[2][0] + b.m[1][1] * a.m[2][1] + b.m[2][1]
                * a.m[2][2] + b.m[3][1] * a.m[2][3], b.m[0][2]
                * a.m[2][0] + b.m[1][2] * a.m[2][1] + b.m[2][2]
                * a.m[2][2] + b.m[3][2] * a.m[2][3], b.m[0][3]
                * a.m[2][0] + b.m[1][3] * a.m[2][1] + b.m[2][3]
                * a.m[2][2] + b.m[3][3] * a.m[2][3], b.m[0][0]
                * a.m[3][0] + b.m[1][0] * a.m[3][1] + b.m[2][0]
                * a.m[3][2] + b.m[3][0] * a.m[3][3], b.m[0][1]
                * a.m[3][0] + b.m[1][1] * a.m[3][1] + b.m[2][1]
                * a.m[3][2] + b.m[3][1] * a.m[3][3], b.m[0][2]
                * a.m[3][0] + b.m[1][2] * a.m[3][1] + b.m[2][2]
                * a.m[3][2] + b.m[3][2] * a.m[3][3], b.m[0][3]
                * a.m[3][0] + b.m[1][3] * a.m[3][1] + b.m[2][3]
                * a.m[3][2] + b.m[3][3] * a.m[3][3]
        );
    }

    /**
     * Multiply a matrix with a rotation quaternion. (this * q)
     *
     * @param q The quaterion.
     * @return This matrix.
     */
    public Matrix multiply(Quaternion q) {
        return multiply(q.toMatrix());
    }

    /**
     * Multiply a matrix with a rotation quaternion. (q * this)
     *
     * @param q The quaterion.
     * @return This matrix.
     */
    public Matrix multiplyLeft(Quaternion q) {
        return multiplyLeft(q.toMatrix());
    }

    /**
     * The euler rotation around the x-Axis.
     *
     * @return The euler rotation around the x-Axis.
     */
    public final double getXEuler() {
        return toQuaternion().getXEuler();
    }

    /**
     * The euler rotation around the y-Axis.
     *
     * @return The euler rotation around the y-Axis.
     */
    public final double getYEuler() {
        return toQuaternion().getYEuler();
    }

    /**
     * The euler rotation around the z-Axis.
     *
     * @return The euler rotation around the z-Axis.
     */
    public final double getZEuler() {
        return toQuaternion().getZEuler();
    }

    /**
     * Get the euler angles in a complete vector.
     *
     * @return The euler rotations around all axis in a vector.
     */
    public final Vector3d getEuler() {
        return new Vector3d(getXEuler(), getYEuler(), getZEuler());
    }

    /**
     * Get the double array containing the matrix values.
     *
     * @return The matrix values.
     */
    public final double[][] getArray() {
        return m;
    }

    /**
     * Convert the Matrix into a DoubleBuffer.
     *
     * @param db The double buffer in which to write the values.
     * @return A DoubleBuffer containing the matrix.
     */
    public final DoubleBuffer getValues(final DoubleBuffer db) {
        double[] tmp = new double[N * N];
        int i = 0;
        for (int x = 0; x < m.length; x++) {
            for (int y = 0; y < m[x].length; y++) {
                tmp[i++] = m[x][y];
            }
        }

        DoubleBuffer localDB = db;
        if (localDB == null) {
            localDB = BufferUtils.createDoubleBuffer(N * N);
        } else {
            localDB.clear();
        }
        localDB.put(tmp);
        return localDB;
    }

    /**
     * Convert the Matrix into a DoubleBuffer.
     *
     * @param fb The double buffer in which to write the values.
     * @return A DoubleBuffer containing the matrix.
     */
    public final FloatBuffer getValuesF(final FloatBuffer fb) {
        float[] tmp = new float[N * N];
        int i = 0;
        for (int x = 0; x < m.length; x++) {
            for (int y = 0; y < m[x].length; y++) {
                tmp[i++] = (float)m[x][y];
            }
        }

        FloatBuffer localFB = fb;
        if (localFB == null) {
            localFB = BufferUtils.createFloatBuffer(N * N);
        } else {
            localFB.clear();
        }
        localFB.put(tmp);
        return localFB;
    }

    /**
     * Get a lwjgl Matrix4f.
     * @return The lwjgl matrix.
     */
    public final Matrix4f getMatrix4f() {
        Matrix4f result = new Matrix4f();
        result.m00 = (float) m[0][0];
        result.m01 = (float) m[0][1];
        result.m02 = (float) m[0][2];
        result.m03 = (float) m[0][3];
        result.m10 = (float) m[1][0];
        result.m11 = (float) m[1][1];
        result.m12 = (float) m[1][2];
        result.m13 = (float) m[1][3];
        result.m20 = (float) m[2][0];
        result.m21 = (float) m[2][1];
        result.m22 = (float) m[2][2];
        result.m23 = (float) m[2][3];
        result.m30 = (float) m[3][0];
        result.m31 = (float) m[3][1];
        result.m32 = (float) m[3][2];
        result.m33 = (float) m[3][3];
        return result;
    }

    /**
     * Transform rotationmatrix to quaterion.
     *
     * This ignores the translation component of the matrix.
     *
     * @return The quaterion.
     */
    public Quaternion toQuaternion() {
        double s = (1.0/2.0) * Math.sqrt(1.0 + m[0][0] + m[1][1] + m[2][2]);
        double x = (m[3][2] - m[2][3]) / (4.0 * s);
        double y = (m[1][3] - m[3][1]) / (4.0 * s);
        double z = (m[2][1] - m[1][2]) / (4.0 * s);
        return new Quaternion(s,x,y,z);
    }

    @Override
    public Matrix clone() {
        Matrix result = new Matrix();
        return result.set(this);
    }
}
