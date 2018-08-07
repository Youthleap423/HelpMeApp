package com.veeritsolutions.uhelpme.arview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.models.ARViewModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;

import java.util.ArrayList;


public class DataView {

    //   private double[] latitudes = ARView._latitude;
    //private double[] longitudes = ARView._longitude;
    public ArrayList<ARViewModel> arViewList = ARView.arViewList;
    public float addX = 0, addY = 0;
    ArrayList<Integer> nextYofText = new ArrayList<Integer>();
    boolean isFirstEntry;
    int[][] coordinateArray = new int[20][2];
    private LinearLayout[] locationMarkerView;
    private RelativeLayout.LayoutParams[] layoutParams;
    private RelativeLayout.LayoutParams[] subjectImageViewParams;
    private RelativeLayout.LayoutParams[] subjectTextViewParams;
    private TextView[] locationTextView;
    private int[] nextXofText;
    private double[] bearings;
    private float angleToShift;

    // String[] places = ARView._name;
    //  String[] distance = ARView._distance;
    //  String[] photoUrl = ARView._photoUrl;
    // String[] colorCode = ARView._colorCode;
    // float[] ratings = ARView._ratings;
    private float yPosition;
    private Location currentLocation = new Location("provider");
    private Location destinedLocation = new Location("provider");
    /**
     * is the dataView Inited?
     */
    private boolean isInit = false;
    private boolean isDrawing = true;
    private Context _context;
    /**
     * width and height of the dataView
     */
    private int width, height;
    private android.hardware.Camera camera;
    private float yawPrevious;
    private float yaw = 0;
    private float pitch = 0;
    private float roll = 0;
    private DisplayMetrics displayMetrics;
    private RadarView radarPoints;
    private RadarLines lrl = new RadarLines();
    private RadarLines rrl = new RadarLines();
    private float rx = 10, ry = 20;
    private float degreetopixelWidth;
    private float degreetopixelHeight;
    private float pixelstodp;
    private float bearing;
    private Bitmap bmp;
    // int position = 1;
    private View rootView;

    DataView(Context ctx) {
        this._context = ctx;

    }

    boolean isInited() {
        return isInit;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void init(int widthInit, int heightInit, android.hardware.Camera camera, DisplayMetrics displayMetrics, final RelativeLayout rel) {
        try {
            //      latitudes = ARView._latitude;
            //longitudes = ARView._longitude;
            //places = ARView._name;
            //distance = ARView._distance;
            // photoUrl = ARView._photoUrl;
            // colorCode = ARView._colorCode;
            // ratings = ARView._ratings;

            arViewList = ARView.arViewList;

            locationMarkerView = new LinearLayout[arViewList.size()];
            layoutParams = new RelativeLayout.LayoutParams[arViewList.size()];
            subjectImageViewParams = new RelativeLayout.LayoutParams[arViewList.size()];
            subjectTextViewParams = new RelativeLayout.LayoutParams[arViewList.size()];
            locationTextView = new TextView[arViewList.size()];
            nextXofText = new int[arViewList.size()];
            coordinateArray = new int[arViewList.size()][2];

            for (int i = 0; i < arViewList.size(); i++) {

                rootView = LayoutInflater.from(_context).inflate(R.layout.ar_view_data, null, false);

                ARViewModel arViewModel = arViewList.get(i);
                TextView tvName = (TextView) rootView.findViewById(R.id.tv_userName);
                tvName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
                tvName.setText(arViewModel.getFirstName() /*+ " " + arViewModel.getLastName()*/);
                //tvName.setText("Jaymin Soneji");

                TextView tvDistance = (TextView) rootView.findViewById(R.id.tv_distance);
                tvDistance.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
                tvDistance.setText(Math.round(arViewModel.getDistance() * 100.0) / 100.0 + " km");

                ImageView imgIcon = (ImageView) rootView.findViewById(R.id.img_categoryIcon);
                Picasso.with(_context)
                        .load(arViewModel.getCategoryIcon1())
                        //.override(30, 50)
                        .into(imgIcon);

                /*Glide.with(_context)
                        .load(arViewModel.getCategoryIcon1())
                        //.override(30, 50)
                        .into(imgIcon);*/

                RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.rb_rating);
                ratingBar.setRating(arViewModel.getRating());

               /* String str = arViewModel.getCategoryColorCode();
                if (str.length() > 0) {
                    rootView.setBackgroundColor(Color.parseColor(str));
                } else {
                    String colorCode = "#0095d7"*//*.insert(1, "0D")*//*;
                    rootView.setBackgroundColor(Color.parseColor(colorCode));
                }*/
                //rootView.setBackgroundColor(Color.WHITE);
                //rootView.getBackground().setAlpha(45);
                layoutParams[i] = new RelativeLayout.LayoutParams(700, 500);
                //layoutParams[i].width = 700;
                //layoutParams[i].height = 150;
//                subjectTextViewParams[i] = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT + 1);
//                subjectTextViewParams[i].addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//                subjectTextViewParams[i].topMargin = 10;
//                subjectTextViewParams[i].leftMargin = 20;
//                subjectTextViewParams[i].rightMargin = 5;
//                subjectTextViewParams[i] = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                //locationMarkerView[i] = new RelativeLayout(_context);
//                locationTextView[i] = new TextView(_context);
//                locationTextView[i].setText(checkTextToDisplay(places[i]));
//                locationTextView[i].setTextColor(Color.parseColor("#0095d7"));
//                locationTextView[i].setLayoutParams(subjectTextViewParams[i]);

                layoutParams[i].setMargins(displayMetrics.widthPixels, displayMetrics.heightPixels, 0, 0);
                // layoutParams[i].setLayoutDirection(LinearLayout.HORIZONTAL);
                locationMarkerView[i] = new LinearLayout(_context);
                locationMarkerView[i].setBackgroundResource(android.R.color.transparent);

                //locationMarkerView[i].setLayoutParams(layoutParams[i]);

                locationMarkerView[i].addView(rootView);
                locationMarkerView[i].setTag(arViewList.get(i));
                //locationMarkerView[i].addView(locationTextView[i]);

                // rel.addView(rootView);
                rel.addView(locationMarkerView[i]);
                //locationTextView[i].setClickable(false);
                /*locationTextView[i].setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if ((v.getId() != -1)) {
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationMarkerView[v.getId()].getLayoutParams();
                            Rect rect = new Rect(params.leftMargin, params.topMargin, params.leftMargin + params.width, params.topMargin + params.height);
                            ArrayList<Integer> matchIDs = new ArrayList<>();
                            Rect compRect = new Rect();
                            int index = 0;
                            for (RelativeLayout.LayoutParams layoutparams : layoutParams) {
                                compRect.set(layoutparams.leftMargin, layoutparams.topMargin,
                                        layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
                                if (compRect.intersect(rect)) {
                                    matchIDs.add(index);
                                }
                                index++;
                            }

                            if (matchIDs.size() > 1) {

                            }
                            Toast.makeText(_context, "Number of places here = " + matchIDs.size(), Toast.LENGTH_SHORT).show();

                            locationMarkerView[v.getId()].bringToFront();
                        }

                    }
                });*/

                //position = i;
                locationMarkerView[i].setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        // position = finalI;
                        ARViewModel arViewModel1 = (ARViewModel) v.getTag();
                        PostedJobModel postedJobModel = new PostedJobModel();
                        postedJobModel.setJobPostId((int) arViewModel1.getJobPostId());
//                        LoginUserModel loginUserModel = new LoginUserModel();
//                        loginUserModel.setClientId((int) arViewModel1.getClientId());

                        Intent intent = new Intent(_context, HomeActivity.class);
                        intent.putExtra(Constants.HELP_DATA, postedJobModel);
                        intent.putExtra(Constants.IS_FROM_AR_VIEW, true);
                        _context.startActivity(intent);
                        //                        Bundle bundle = new Bundle();
//                        bundle.putSerializable(Constants.USER_DATA, loginUserModel);
//                        OtherPersonProfileFragment fragment = new OtherPersonProfileFragment();
//                        fragment.setArguments(bundle);
//                        ((AppCompatActivity) _context).getSupportFragmentManager().beginTransaction().replace(rel.getId(), fragment).commit();
                        //HomeActivity homeActivity = new HomeActivity();
                        //homeActivity.getFragmentManager().beginTransaction().replace(R.id.container,new OtherPersonProfileFragment())
                        // homeActivity.pushFragment(new OtherPersonProfileFragment(), false, false, bundle);
                        /*if (v.getId() != -1) {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) locationMarkerView[v.getId()].getLayoutParams();
                            Rect rect = new Rect(params.leftMargin, params.topMargin, params.leftMargin + params.width, params.topMargin + params.height);
                            ArrayList<Integer> matchIDs = new ArrayList<>();
                            Rect compRect = new Rect();
                            int count = 0;
                            int index = 0;
                            for (RelativeLayout.LayoutParams layoutparams : layoutParams) {
                                compRect.set(layoutparams.leftMargin, layoutparams.topMargin,
                                        layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
                                if (compRect.intersect(rect)) {
                                    matchIDs.add(index);
                                    count += 1;
                                }
                                index++;
                            }

                            if (count > 1) {

                            }
                            ToastHelper.getInstance().showMessage("Number of places here = " + count);

                            locationMarkerView[v.getId()].bringToFront();
                        }*/

                    }
                });
            }


            // bmp = BitmapFactory.decodeResource(_context.getResources(), R.drawable.img_launcher_icon);

            this.displayMetrics = displayMetrics;
            this.degreetopixelWidth = this.displayMetrics.widthPixels / camera.getParameters().getHorizontalViewAngle();
            this.degreetopixelHeight = this.displayMetrics.heightPixels / camera.getParameters().getVerticalViewAngle();
            Debug.trace("camera.getParameters().getHorizontalViewAngle()==" + camera.getParameters().getHorizontalViewAngle());

            bearings = new double[arViewList.size()];

            double latitude = PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0);
            double longitude = PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0);
            currentLocation.setLatitude(latitude);
            currentLocation.setLongitude(longitude);


            if (bearing < 0)
                bearing = 360 + bearing;

            for (int i = 0; i < arViewList.size(); i++) {
                destinedLocation.setLatitude(arViewList.get(i).getLatitude());
                destinedLocation.setLongitude(arViewList.get(i).getLongitude());
                bearing = currentLocation.bearingTo(destinedLocation);

                if (bearing < 0) {
                    bearing = 360 + bearing;
                }
                bearings[i] = bearing;

            }
            radarPoints = new RadarView(this, bearings);
            radarPoints.dataView = this;

            this.camera = camera;
            width = widthInit;
            height = heightInit;

            lrl.set(0, -RadarView.RADIUS);
            lrl.rotate(Camera.DEFAULT_VIEW_ANGLE / 2);
            lrl.add(rx + RadarView.RADIUS, ry + RadarView.RADIUS);
            rrl.set(0, -RadarView.RADIUS);
            rrl.rotate(-Camera.DEFAULT_VIEW_ANGLE / 2);
            rrl.add(rx + RadarView.RADIUS, ry + RadarView.RADIUS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

		/*
         * initialization is done, so dont call init() again.
		 * */
        isInit = true;
    }

    void draw(PaintUtils dw, float yaw, float pitch, float roll) {

        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;

        // Draw Radar
        String dirTxt = "";
        int bearing = (int) this.yaw;
        int range = (int) (this.yaw / (360f / 16f));
        if (range == 15 || range == 0) dirTxt = "N";
        else if (range == 1 || range == 2) dirTxt = "NE";
        else if (range == 3 || range == 4) dirTxt = "E";
        else if (range == 5 || range == 6) dirTxt = "SE";
        else if (range == 7 || range == 8) dirTxt = "S";
        else if (range == 9 || range == 10) dirTxt = "SW";
        else if (range == 11 || range == 12) dirTxt = "W";
        else if (range == 13 || range == 14) dirTxt = "NW";

        //radarPoints = new RadarView(this, bearings);

        radarPoints.dataView = this;

        dw.paintObj(radarPoints, rx + PaintUtils.XPADDING, ry + PaintUtils.YPADDING, -this.yaw, 1, this.yaw);
        dw.setFill(false);
        dw.setColor(Color.argb(100, 220, 0, 0));
        dw.paintLine(lrl.x, lrl.y, rx + RadarView.RADIUS, ry + RadarView.RADIUS);
        dw.paintLine(rrl.x, rrl.y, rx + RadarView.RADIUS, ry + RadarView.RADIUS);
        dw.setColor(Color.rgb(255, 255, 255));
        dw.setFontSize(12);
        radarText(dw, "" + bearing + ((char) 200) + " " + dirTxt, rx + RadarView.RADIUS, ry - 5, true, false, -1);


        drawTextBlock(dw);
    }

    void drawPOI(PaintUtils dw, float yaw) {
        if (isDrawing) {
            dw.paintObj(radarPoints, rx + PaintUtils.XPADDING, ry + PaintUtils.YPADDING, -this.yaw, 1, this.yaw);
            isDrawing = false;
        }
    }

    private void radarText(PaintUtils dw, String txt, float x, float y, boolean bg, boolean isLocationBlock, int count) {

        float padw = 4, padh = 2;
        float w = dw.getTextWidth(txt) + padw * 2;
        float h;
        if (isLocationBlock) {
            h = dw.getTextAsc() + dw.getTextDesc() + padh * 2 + 10;
        } else {
            h = dw.getTextAsc() + dw.getTextDesc() + padh * 2;
        }
        if (bg) {

            if (isLocationBlock) {
                layoutParams[count].setMargins((int) (x - w / 2 - 10), (int) (y - h / 2 - 10), 0, 0);
                layoutParams[count].height = 500;
                layoutParams[count].width = 2000;
                locationMarkerView[count].setLayoutParams(layoutParams[count]);

            } else {
                dw.setColor(Color.rgb(0, 0, 0));
                dw.setFill(true);
                dw.paintRect((x - w / 2) + PaintUtils.XPADDING, (y - h / 2) + PaintUtils.YPADDING, w, h);
                pixelstodp = (padw + x - w / 2) / ((displayMetrics.density) / 160);
                dw.setColor(Color.rgb(255, 255, 255));
                dw.setFill(false);
                dw.paintText((padw + x - w / 2) + PaintUtils.XPADDING, ((padh + dw.getTextAsc() + y - h / 2)) + PaintUtils.YPADDING, txt);
            }
        }

    }

    private String checkTextToDisplay(String str) {

        if (str.length() > 15) {
            str = str.substring(0, 15) + "...";
        }
        return str;

    }

    private void drawTextBlock(PaintUtils dw) {

        for (int i = 0; i < bearings.length; i++) {
            if (bearings[i] < 0) {

                if (this.pitch != 90) {
                    yPosition = (this.pitch - 90) * this.degreetopixelHeight + 200;
                } else {
                    yPosition = (float) this.height / 2;
                }

                bearings[i] = 360 - bearings[i];
                angleToShift = (float) bearings[i] - this.yaw;
                nextXofText[i] = (int) (angleToShift * degreetopixelWidth);
                yawPrevious = this.yaw;
                isDrawing = true;
                radarText(dw, arViewList.get(i).getFirstName()/* + " " + arViewList.get(i).getLastName()*/, nextXofText[i], yPosition, true, true, i);
                coordinateArray[i][0] = nextXofText[i];
                coordinateArray[i][1] = (int) yPosition;

            } else {
                angleToShift = (float) bearings[i] - this.yaw;

                if (this.pitch != 90) {
                    yPosition = (this.pitch - 90) * this.degreetopixelHeight + 200;
                } else {
                    yPosition = (float) this.height / 2;
                }


                nextXofText[i] = (int) ((displayMetrics.widthPixels / 2) + (angleToShift * degreetopixelWidth));
                if (Math.abs(coordinateArray[i][0] - nextXofText[i]) > 50) {
                    radarText(dw, arViewList.get(i).getFirstName() /*+ " " + arViewList.get(i).getLastName()*/, (nextXofText[i]), yPosition, true, true, i);
                    coordinateArray[i][0] = (int) ((displayMetrics.widthPixels / 2) + (angleToShift * degreetopixelWidth));
                    coordinateArray[i][1] = (int) yPosition;

                    isDrawing = true;
                } else {
                    radarText(dw, arViewList.get(i).getFirstName() /*+ " " + arViewList.get(i).getLastName()*/, coordinateArray[i][0], yPosition, true, true, i);
                    isDrawing = false;
                }
            }
        }
    }
}