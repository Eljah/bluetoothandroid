package ru;

/**
 * Created by eljah32 on 2/29/2016.
 */
public class TrackerTransferObject implements TransferObject {
    String longitude;
    String latitude;
    String diameter;

    TrackerTransferObject(String longitude, String latitude, String diameter)
    {
        this.longitude=longitude;
        this.diameter=diameter;
        this.latitude=latitude;
    }

    public void update(String longitude, String latitude, String diameter)
    {
        this.longitude=longitude;
        this.diameter=diameter;
        this.latitude=latitude;
    }

    public byte[] getBytes() {
        return (longitude+" "+latitude+ " "+diameter).getBytes();
    }
}
