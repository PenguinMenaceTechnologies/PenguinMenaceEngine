package net.pme.core.math;


import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Testcases for quaternion.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class QuaternionTest {
    @Before
    public void before() {

    }

    @After
    public void after() {

    }

    @Test
    public void identity() {
        Quaternion q = new Quaternion(1, 0 ,0, 0);
        Vector3d v = new Vector3d(1,1,1);
        Vector3d r = v.clone().transformCoords(q);

        Assert.assertTrue("Identiy does change vector", v.equals(r));
    }

    @Test
    public void toMatrix() {
        Quaternion q = new Quaternion(1, 0, 0, 0);
        Assert.assertTrue("Not right matrix", q.toMatrix().equals(Matrix.identity()));
    }

    @Test
    public void conjugate() {
        Quaternion q = new Quaternion(1, 1, 1, 1);
        Quaternion r = new Quaternion(1, -1, -1, -1);
        Assert.assertTrue("Not right conjugation.", r.equals(q.conjugate()));
    }

    @Test
    public void length() {
        Quaternion q = new Quaternion(1,1,1,1);
        Assert.assertTrue("Not valid length.", MathUtils.isEqual(q.length(), 2));
        Assert.assertTrue("Not valid length.", MathUtils.isEqual(q.length2(), 4));
    }

    @Test
    public void normalize() {
        Quaternion q = new Quaternion(20, 0, 0, 0);
        Quaternion norm = new Quaternion(1, 0, 0, 0);
        Assert.assertTrue("Not normalized.", q.normalize().equals(norm));
    }

    @Test
    public void rotate180() {
        Quaternion q = new Quaternion(0, 0, 0, 1);
        Vector3d v = new Vector3d(1, 0, 0);
        Vector3d res = new Vector3d(1, 0, 0);
        res.transformCoords(q);
        res.transformCoords(q);
        Assert.assertTrue("Not correct at rotation.", res.equals(v));
    }

    @Test
    public void transformX90Matrix() {
        Matrix r = Matrix.rotationAxis(new Vector3d(1, 0, 0), Math.PI / 2);
        double sin = Math.sin(Math.PI / 4);
        double cos = Math.cos(Math.PI / 4);
        Quaternion q = new Quaternion(cos, sin, 0, 0);
        Assert.assertTrue("Matrix transformation wrong.\n" + q.toMatrix().toString() + "\n" + r.toString(), q.toMatrix().equals(r));
    }

    @Test
    public void transformY90Matrix() {
        Matrix r = Matrix.rotationAxis(new Vector3d(0, 1, 0), Math.PI / 2);
        double sin = Math.sin(Math.PI / 4);
        double cos = Math.cos(Math.PI / 4);
        Quaternion q = new Quaternion(cos, 0, sin, 0);
        Assert.assertTrue("Matrix transformation wrong.\n" + q.toMatrix().toString() + "\n" + r.toString(), q.toMatrix().equals(r));
    }

    @Test
    public void transformZ90Matrix() {
        Matrix r = Matrix.rotationAxis(new Vector3d(0, 0, 1), Math.PI / 2);
        double sin = Math.sin(Math.PI / 4);
        double cos = Math.cos(Math.PI / 4);
        Quaternion q = new Quaternion(cos, 0, 0, sin);
        Assert.assertTrue("Matrix transformation wrong.\n" + q.toMatrix().toString() + "\n" + r.toString(), q.toMatrix().equals(r));
    }

    @Test
    public void transformMatrix() {
        for (int i = 0; i < 100; i++) {
            double a = Math.random() * Math.PI * 2;
            double x = Math.random();
            double y = Math.random();
            double z = (x != 0 || y != 0) ? Math.random() : 1.0;
            Vector3d v = new Vector3d(x, y, z);
            v.normalize();
            double sin = Math.sin(a/2);
            Quaternion q = new Quaternion(Math.cos(a/2), sin * v.getX(), sin * v.getY(), sin * v.getZ());
            Matrix r = Matrix.rotationAxis(v, a);
            Assert.assertTrue("Matrix transformation wrong.\n" + q.toMatrix().toString() + "\n" + r.toString(), q.toMatrix().equals(r));
        }
    }

    @Test
    public void transformMatrixAndBack() {
        for (int i = 0; i < 100; i++) {
            double a = Math.random() * Math.PI * 2;
            double x = Math.random();
            double y = Math.random();
            double z = (x != 0 || y != 0) ? Math.random() : 1.0;
            Vector3d v = new Vector3d(x, y, z);
            v.normalize();
            double sin = Math.sin(a);
            Quaternion q = new Quaternion(Math.cos(a), sin * v.getX(), sin * v.getY(), sin * v.getZ());
            Assert.assertTrue("Matrix transformation wrong.\n" + q.toString() + "\n" +  q.toMatrix().toQuaternion().toString(), q.toMatrix().toQuaternion().equals(q));
        }
    }

    @Test
    public void transform180MatrixAndBack() {
        Quaternion q = new Quaternion(0, 0, 0, 1);
        Assert.assertTrue("Matrix transformation wrong.", q.toMatrix().toQuaternion().equals(q));
    }

    @Test
    public void testGetEulerX() {
        for (int i = 0; i < 100; i++) {
            double x = Math.random()*2*Math.PI - Math.PI;

            Quaternion q = new Quaternion(Math.cos(x/2), Math.sin(x/2), 0,0);
            // Beware of pitch only in range of Math.PI / 2 and -Math.PI / 2
            Assert.assertEquals("Invalid angle: ", Math.sin(x), Math.sin(q.getXEuler()), 10E-8);
        }
    }

    @Test
    public void testGetEulerY() {
        for (int i = 0; i < 100; i++) {
            double x = Math.random()*2*Math.PI - Math.PI;

            Quaternion q = new Quaternion(Math.cos(x/2), 0, Math.sin(x/2), 0);
            Assert.assertEquals("Invalid angle: ", x, q.getYEuler(), 10E-8);
        }
    }

    @Test
    public void testGetEulerZ() {
        for (int i = 0; i < 100; i++) {
            double x = Math.random()*2*Math.PI - Math.PI;

            Quaternion q = new Quaternion(Math.cos(x/2), 0,0, Math.sin(x/2));
            Assert.assertEquals("Invalid angle: ", x, q.getZEuler(), 10E-8);
        }
    }
}
