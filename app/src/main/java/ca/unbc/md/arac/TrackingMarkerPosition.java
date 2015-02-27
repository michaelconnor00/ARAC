package ca.unbc.md.arac;

import com.metaio.sdk.jni.Rotation;

public class TrackingMarkerPosition {

    public float x_offset;
    public float y_offset;
    public float z_offset;
    public Rotation rotation;

    public TrackingMarkerPosition(float x_offset, float y_offset, float z_offset, Rotation rotation){
        this.x_offset = x_offset;
        this.y_offset = y_offset;
        this.z_offset = z_offset;
        this.rotation = rotation;
    }
}
