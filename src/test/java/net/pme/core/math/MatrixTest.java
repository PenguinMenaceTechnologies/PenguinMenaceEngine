package net.pme.core.math;

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

    @Test
    public void testDet() {
        Assert.assertEquals("Identity determinant not 1", 1.0, Matrix.identity().det(), 10E-8);

        for (int count = 0; count < 100; count++) {
            double x = Math.random();
            double y = Math.random();
            double z = x != 0.0 || y != 0.0 ? Math.random() : 1;

            Matrix r = Matrix.rotationAxis(new Vector3d(x, y, z).normalize(), Math.PI);

            Assert.assertEquals("Rotation determinant not 1:\n" + r.toString(), 1.0, r.det(), 10E-8);
        }
    }

    @Test
    public void testInvertIdentity() {
        Matrix id = Matrix.identity();
        Assert.assertTrue("Wrong inversion.", Matrix.identity().invert().equals(id));
    }


    @Test
    public void testInvertRotation() {
        Matrix id = Matrix.identity();
        for (int count = 0; count < 100; count++) {
            double x = Math.random();
            double y = Math.random();
            double z = x != 0.0 || y != 0.0 ? Math.random() : 1;

            Matrix r = Matrix.rotationAxis(new Vector3d(x, y, z).normalize(), Math.PI);

            Assert.assertTrue("Wrong inversion for rotation.", r.clone().invert().multiply(r).equals(id));
        }
    }

    @Test
    public void testGetEulerX() {
        for (int i = 0; i < 100; i++) {
            double x = Math.random()*2*Math.PI - Math.PI;

            Matrix r = Matrix.rotationAxis(new Vector3d(1, 0, 0), x);
            // Beware of pitch only in range of Math.PI / 2 and -Math.PI / 2
            Assert.assertEquals("Invalid angle: ", Math.sin(x), Math.sin(r.getXEuler()), 10E-8);
        }
    }

    @Test
    public void testGetEulerY() {
        for (int i = 0; i < 100; i++) {
            double x = Math.random()*2*Math.PI - Math.PI;

            Matrix r = Matrix.rotationAxis(new Vector3d(0, 1, 0), x);
            Assert.assertEquals("Invalid angle: ", x, r.getYEuler(), 10E-8);
        }
    }

    @Test
    public void testGetEulerZ() {
        for (int i = 0; i < 100; i++) {
            double x = Math.random()*2*Math.PI - Math.PI;

            Matrix r = Matrix.rotationAxis(new Vector3d(0, 0, 1), x);
            Assert.assertEquals("Invalid angle: ", x, r.getZEuler(), 10E-8);
        }
    }
}
