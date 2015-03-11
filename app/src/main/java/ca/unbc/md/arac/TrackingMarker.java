package ca.unbc.md.arac;

import com.metaio.sdk.jni.IGeometry;

public class TrackingMarker {

    public int marker_ID;
    public IGeometry marker_3d_content;
    boolean tracking_state;
    double tracking_quality = 0.0;

    public TrackingMarker(int marker_ID, IGeometry marker_3d_content){
        this.marker_ID = marker_ID;
        this.marker_3d_content = marker_3d_content;
    }

}
