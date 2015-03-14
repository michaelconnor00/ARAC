package ca.unbc.md.arac;

import com.metaio.sdk.jni.IGeometry;

public class TrackingMarker {

    protected int marker_ID;
    protected IGeometry marker_3d_content;

    protected boolean tracking_state;
    protected double tracking_quality = 0.0;

    public TrackingMarker(int marker_ID, IGeometry marker_3d_content){
        this.marker_ID = marker_ID;
        this.marker_3d_content = marker_3d_content;
    }

}
