package net.pme;

import net.pme.core.math.Matrix;
import net.pme.core.math.Vector3d;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Testcases for matrix.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class MatrixTest {
    @Before
    public void before() {

    }

    @After
    public void after() {

    }

    @Test
    public void testIdentity() {
        Matrix id = Matrix.identity();
        double[][] m = id.getArray();
        for(int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                Assert.assertTrue("Wrong value at ("+i+"|"+j+")", (i == j && m[i][j] == 1) || (i != j && m[i][j] == 0));
            }
        }
    }

    @Test
    public void testRotationAxisNone() {
        for (int count = 0; count < 100; count++) {
            double x = Math.random();
            double y = Math.random();
            double z = x != 0.0 || y != 0.0 ? Math.random() : 1;

            Matrix r = Matrix.rotationAxis(new Vector3d(x, y, z).normalize(), 0);

            Assert.assertTrue("Matrix not identical:\n"+r.toString(), r.equals(Matrix.identity()));
        }
    }

    @Test
    public void testRotationAxisX180() {
        Matrix reference = new Matrix(
                1,  0,  0, 0,
                0, -1,  0, 0,
                0,  0, -1, 0,
                0,  0,  0, 1);

        Matrix r = Matrix.rotationAxis(new Vector3d(1, 0, 0), Math.PI);

        Assert.assertTrue("Matrix not identical:\n"+r.toString(), r.equals(reference));
    }

    @Test
    public void testRotationAxisY180() {
        Matrix reference = new Matrix(
                -1, 0,  0, 0,
                0,  1,  0, 0,
                0,  0, -1, 0,
                0,  0,  0, 1);

        Matrix r = Matrix.rotationAxis(new Vector3d(0, 1, 0), Math.PI);

        Assert.assertTrue("Matrix not identical:\n"+r.toString(), r.equals(reference));
    }

    @Test
    public void testRotationAxisZ180() {
        Matrix reference = new Matrix(
                -1, 0, 0, 0,
                0, -1, 0, 0,
                0,  0, 1, 0,
                0,  0, 0, 1);

        Matrix r = Matrix.rotationAxis(new Vector3d(0, 0, 1), Math.PI);

        Assert.assertTrue("Matrix not identical:\n"+r.toString(), r.equals(reference));
    }

    @Test
    public void testRotationAxisX90() {
        Matrix reference = new Matrix(
                1,  0,  0, 0,
                0,  0,  1, 0,
                0, -1,  0, 0,
                0,  0,  0, 1);

        Matrix r = Matrix.rotationAxis(new Vector3d(1, 0, 0), Math.PI / 2);

        Assert.assertTrue("Matrix not identical:\n"+r.toString(), r.equals(reference));
    }

    @Test
    public void testRotationAxisY90() {
        Matrix reference = new Matrix(
                0, 0, -1, 0,
                0, 1,  0, 0,
                1, 0,  0, 0,
                0, 0,  0, 1);

        Matrix r = Matrix.rotationAxis(new Vector3d(0, 1, 0), Math.PI / 2);

        Assert.assertTrue("Matrix not identical:\n"+r.toString(), r.equals(reference));
    }

    @Test
    public void testRotationAxisZ90() {
        Matrix reference = new Matrix(
                0,  1, 0, 0,
                -1, 0, 0, 0,
                0,  0, 1, 0,
                0,  0, 0, 1);

        Matrix r = Matrix.rotationAxis(new Vector3d(0, 0, 1), Math.PI / 2);

        Assert.assertTrue("Matrix not identical:\n"+r.toString(), r.equals(reference));
    }

    @Test
    public void multiplyRotation() {
        for (int count = 0; count < 100; count++) {
            double x = Math.random();
            double y = Math.random();
            double z = x != 0.0 || y != 0.0 ? Math.random() : 1;

            Matrix r = Matrix.rotationAxis(new Vector3d(x, y, z).normalize(), Math.PI);

            r.multiply(r);

            Assert.assertTrue("Matrix not identical:\n"+r.toString(), r.equals(Matrix.identity()));
        }
    }
}
