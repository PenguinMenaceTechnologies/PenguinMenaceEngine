package net.pme.graphics.data;

import org.lwjgl.util.Color;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-13
 */
public class Material {
    private Color ambientColor;
    private Color diffuseColor;
    private Color specularColor;
    private float specularCoeffient;
    private float transparancy;
    private Texture ambientTexture;
    private Texture diffuseTexture;
    private Texture specularColorTexture;
    private Texture specularCoeffientTexture;
    private Texture alphaTexture;
    private Texture bumpMap;
    private Texture displacementMap;
    private Texture stencilTexture;

    public Material() {
        this.transparancy = 1.0f;
        this.specularCoeffient = 1.0f;
    }

    public Texture getSpecularCoeffientTexture() {
        return specularCoeffientTexture;
    }

    public void setSpecularCoeffientTexture(Texture specularCoeffientTexture) {
        this.specularCoeffientTexture = specularCoeffientTexture;
    }

    public Color getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Color ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Color getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Color diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Color getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Color specularColor) {
        this.specularColor = specularColor;
    }

    public float getSpecularCoeffient() {
        return specularCoeffient;
    }

    public void setSpecularCoeffient(float specularCoeffient) {
        this.specularCoeffient = specularCoeffient;
    }

    public float getTransparancy() {
        return transparancy;
    }

    public void setTransparancy(float transparancy) {
        this.transparancy = transparancy;
    }

    public Texture getAmbientTexture() {
        return ambientTexture;
    }

    public void setAmbientTexture(Texture ambientTexture) {
        this.ambientTexture = ambientTexture;
    }

    public Texture getDiffuseTexture() {
        return diffuseTexture;
    }

    public void setDiffuseTexture(Texture diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
    }

    public Texture getSpecularColorTexture() {
        return specularColorTexture;
    }

    public void setSpecularColorTexture(Texture specularColorTexture) {
        this.specularColorTexture = specularColorTexture;
    }

    public Texture getAlphaTexture() {
        return alphaTexture;
    }

    public void setAlphaTexture(Texture alphaTexture) {
        this.alphaTexture = alphaTexture;
    }

    public Texture getBumpMap() {
        return bumpMap;
    }

    public void setBumpMap(Texture bumpMap) {
        this.bumpMap = bumpMap;
    }

    public Texture getDisplacementMap() {
        return displacementMap;
    }

    public void setDisplacementMap(Texture displacementMap) {
        this.displacementMap = displacementMap;
    }

    public Texture getStencilTexture() {
        return stencilTexture;
    }

    public void setStencilTexture(Texture stencilTexture) {
        this.stencilTexture = stencilTexture;
    }

    public enum IlluminationModel {
        COLOR_ON_AMBIENT_OFF, COLOR_ON_AMBIENT_ON, HIGHLIGHT_ON, REFLECTION_ON_RAYTRACE_ON;
    }
}
