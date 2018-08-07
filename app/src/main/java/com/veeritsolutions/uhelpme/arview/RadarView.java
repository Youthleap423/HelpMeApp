package com.veeritsolutions.uhelpme.arview;

import android.graphics.Color;
import android.location.Location;

import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.models.ARViewModel;

import java.util.ArrayList;


public class RadarView {
    /**
     * The screen
     */
    public DataView dataView;
    /**
     * The radar's range
     */
    float range;
    /**
     * Radius in pixel on screen
     */
    public static float RADIUS = 40;
    /**
     * Position on screen
     */
    static float originX = 0, originY = 0;

    /**
     * You can change the radar color from here.
     */
    private static int radarColor = Color.parseColor("#0095d7");

    private Location currentLocation = new Location("provider");
    private Location destinedLocation = new Location("provider");


    //private double[] latitudes = ARView._latitude;
    // private double[] longitudes = ARView._longitude;

    private ArrayList<ARViewModel> arViewList = ARView.arViewList;
    private float[][] coordinateArray = new float[arViewList.size()][2];

    private float angleToShift;
    public float degreetopixel;
    private float bearing;
    private float circleOriginX;
    private float circleOriginY;
    private float mscale;


    public float x = 0;
    private float y = 0;
    private float z = 0;

    private float yaw = 0;
    private double[] bearings;
    private ARView arView = new ARView();

    RadarView(DataView dataView, double[] bearings) {
        this.bearings = bearings;
        this.dataView = dataView;
        calculateMetrics();
    }

    private void calculateMetrics() {
        circleOriginX = originX + RADIUS;
        circleOriginY = originY + RADIUS;

        range = (float) arView.convertToPix(10) * 50;
        mscale = range / arView.convertToPix((int) RADIUS);
    }

    void paint(PaintUtils dw, float yaw) {

//		circleOriginX = originX + RADIUS;
//		circleOriginY = originY + RADIUS;
        this.yaw = yaw;
//		range = arView.convertToPix(10) * 1000;		/** Draw the radar */
        dw.setFill(true);
        dw.setColor(radarColor);
        dw.paintCircle(originX + RADIUS, originY + RADIUS, RADIUS);

        /** put the markers in it */
//		float scale = range / arView.convertToPix((int)RADIUS);
        /**
         *  Your current location coordinate here.
         * */
        double latitude = PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0);
        double longitude = PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0);
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);


        for (int i = 0; i < arViewList.size(); i++) {
            destinedLocation.setLatitude(arViewList.get(i).getLatitude());
            destinedLocation.setLongitude(arViewList.get(i).getLongitude());
            convLocToVec(currentLocation, destinedLocation);
            float x = this.x / mscale;
            float y = this.z / mscale;

            if (x * x + y * y < RADIUS * RADIUS) {
                dw.setFill(true);
                dw.setColor(Color.rgb(255, 255, 255));
                dw.paintRect(x + RADIUS, y + RADIUS, 2, 2);
            }
        }
    }

    public void calculateDistances(PaintUtils dw, float yaw) {
        currentLocation.setLatitude(23.012053546054684);
        currentLocation.setLongitude(72.50395287258371);
        for (int i = 0; i < arViewList.size(); i++) {
            if (bearings[i] < 0) {
                bearings[i] = 360 - bearings[i];
            }
            if (Math.abs(coordinateArray[i][0] - yaw) > 3) {
                angleToShift = (float) bearings[i] - this.yaw;
                coordinateArray[i][0] = this.yaw;
            } else {
                angleToShift = (float) bearings[i] - coordinateArray[i][0];

            }
            destinedLocation.setLatitude(arViewList.get(i).getLatitude());
            destinedLocation.setLongitude(arViewList.get(i).getLongitude());
            float[] z = new float[1];
            z[0] = 0;
            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), destinedLocation.getLatitude(), destinedLocation.getLongitude(), z);
            bearing = currentLocation.bearingTo(destinedLocation);

            this.x = (float) (circleOriginX + 40 * (Math.cos(angleToShift)));
            this.y = (float) (circleOriginY + 40 * (Math.sin(angleToShift)));


            if (x * x + y * y < RADIUS * RADIUS) {
                dw.setFill(true);
                dw.setColor(Color.rgb(255, 255, 255));
                dw.paintRect(x + RADIUS - 1, y + RADIUS - 1, 2, 2);
            }
        }
    }

    /**
     * Width on screen
     */
    public float getWidth() {
        return RADIUS * 2;
    }

    /**
     * Height on screen
     */
    public float getHeight() {
        return RADIUS * 2;
    }


    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void convLocToVec(Location source, Location destination) {
        float[] z = new float[1];
        z[0] = 0;
        Location.distanceBetween(source.getLatitude(), source.getLongitude(), destination
                .getLatitude(), source.getLongitude(), z);
        float[] x = new float[1];
        Location.distanceBetween(source.getLatitude(), source.getLongitude(), source
                .getLatitude(), destination.getLongitude(), x);
        if (source.getLatitude() < destination.getLatitude())
            z[0] *= -1;
        if (source.getLongitude() > destination.getLongitude())
            x[0] *= -1;

        set(x[0], (float) 0, z[0]);
    }
}