package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Classifier extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private TextureView textureView;
    private BottomSheetBehavior mBottomSheetBehavior;

    private ImageView Bottom_sheet_arrow;
    private CameraDevice cameraDevice;
    private Size imageDimension;
    CaptureRequest.Builder captureRequestBuilder;
    private Size size;
    private ImageReader imageReader;
    private File file;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraCaptureSession cameraCaptureSessions;
    private Interpreter tflite;
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    private List<String> labelList  ;

    private String[] topLabels = null;
    private String[] topConfidence = null;
    private int DIM_IMG_SIZE_X = 160;
    private int DIM_IMG_SIZE_Y = 160;
    private int DIM_PIXEL_SIZE = 3;
    private int intValues[];
    private static final int RESULTS_TO_SHOW = 3;
    private float[][] labelProbArray = null;




    private String cameraId;
    private HandlerThread mbackgroundThread;
    private Context context;


    private View view;
    private ImageButton capturebutton;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private ByteBuffer imgData = null;

    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    private Activity activity;
    private CameraCaptureSession cameraCaptureSession;
    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice=null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag2_layout);
        Bottom_sheet_arrow = (ImageView) findViewById(R.id.bottom_sheet_arrow);
        textureView = (TextureView) findViewById(R.id.texture_View);
        capturebutton = (ImageButton) findViewById(R.id.capture);
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));





        mBottomSheetBehavior.setBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View view, int new_state) {
                        switch (new_state) {
                            case BottomSheetBehavior.STATE_EXPANDED: {
                                Bottom_sheet_arrow.setImageResource(R.drawable.icn_chevron_down);
                                break;
                            }
                            case BottomSheetBehavior.STATE_COLLAPSED:
                                Bottom_sheet_arrow.setImageResource(R.drawable.icn_chevron_up);
                                break;

                            case BottomSheetBehavior.STATE_DRAGGING: {
                                Bottom_sheet_arrow.setImageResource(R.drawable.ic_drag_handle_black_24dp);
                            }
                        }
                    }


                    @Override
                    public void onSlide(@NonNull View view, float v) {

                    }
                }



        );
        textureView.setSurfaceTextureListener(textureListener);


        try{
            tflite = new Interpreter(loadModelFile(), tfliteOptions);
            labelList = loadLabelList();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];
        imgData =
                ByteBuffer.allocateDirect(
                        4 * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());
        labelProbArray = new float[1][labelList.size()];

        //labelProbArray = new float[1][labelList.size()];
        capturebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takePicture();
                convertBitmapToByteBuffer(bitmap);
                try{
                    tflite.run(imgData, labelProbArray);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });











   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag1_layout);

        View bottomSheet = findViewById(R.id.bottom_sheet);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mTextViewState = findViewById(R.id.text_view_state);

        Button buttonExpand = findViewById(R.id.button_expand);
        Button buttonCollapse = findViewById(R.id.button_collapse);

        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        buttonCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mTextViewState.setText("Collapsed");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mTextViewState.setText("Dragging...");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mTextViewState.setText("Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mTextViewState.setText("Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        mTextViewState.setText("Settling...");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mTextViewState.setText("Sliding...");
            }
        });*/



    }

    void populateLabels()
    {
        labelList.add("Apple___Apple_scab");
        labelList.add(     "Apple___Black_rot");
        labelList.add(       "Apple___Cedar_apple_rust");
        labelList.add("Apple___healthy");
        labelList.add(       "Chery_(including_sour)___Powdery_mildew");
        labelList.add( "Cherry_(including_sour)___healthy");
        labelList.add(     "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot");
        labelList.add(    "Corn_(maize)___Common_rust_");
        labelList.add(    "Corn_(maize)___Northern_Leaf_Blight");
        labelList.add(    "Corn_(maize)___healthy");
        labelList.add(   "Grape___Black_rot");
        labelList.add(     "Grape___Esca_(Black_Measles)");
        labelList.add(   "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)");
        labelList.add(     "Grape___healthy");
        labelList.add(     "Orange___Haunglongbing_(Citrus_greening)");
        labelList.add(     "Peach___Bacterial_spot");
        labelList.add( "Pepper,_bell___Bacterial_spot");
        labelList.add(   "Pepper,_bell___healthy");
        labelList.add(     "Potato___Early_blight");
        labelList.add(   "Potato___Late_blight");
        labelList.add(   "Potato___healthy");
        labelList.add(   "Raspberry___healthy");
        labelList.add(    "Soybean___healthy");
        labelList.add(    "Squash___Powdery_mildew");
        labelList.add(    "Strawberry___Leaf_scorch");
        labelList.add(      "Strawberry___healthy");
        labelList.add(       "Tomato___Bacterial_spot");
        labelList.add(      "Tomato___Early_blight");
        labelList.add(      "Tomato___Late_blight");
        labelList.add(       "Tomato___Leaf_Mold");
        labelList.add(    "Tomato___Septoria_leaf_spot");
        labelList.add(       "Tomato___Spider_mites Two-spotted_spider_mite");
        labelList.add( "Tomato___Target_Spot");
        labelList.add( "Tomato___Tomato_Yellow_Leaf_Curl_Virus");
        labelList.add(   "Tomato___Tomato_mosaic_virus");
        labelList.add(   "Tomato___healthy");
    }




    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("dr_prithvi.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }




    private Bitmap takePicture() {
        Bitmap bitmapTextureView = textureView.getBitmap();
        bitmapTextureView = Bitmap.createScaledBitmap(bitmapTextureView,DIM_IMG_SIZE_X,DIM_IMG_SIZE_Y,false);
        return bitmapTextureView;
    }

    private void createCameraPreview() {
        try{
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert  texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if(cameraDevice == null)
                        return;
                    cameraCaptureSessions = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(activity, "Changed", Toast.LENGTH_SHORT).show();
                }
            },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if(cameraDevice == null)
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),null,mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void openCamera() {
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try{
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            //Check realtime permission if run higher API 23
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId,stateCallback,null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION)
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(activity, "You can't use camera without permission", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if(textureView.isAvailable())
            openCamera();
        else
            textureView.setSurfaceTextureListener(textureListener);
    }

    @Override
    public void onPause() {
        stopBackgroundThread();
        super.onPause();
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread= null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    private List<String> loadLabelList() throws IOException {
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(this.getAssets().open("labels.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        if(labelList ==null)
            Toast.makeText(context,"List empty",Toast.LENGTH_LONG).show();
        return labelList;
    }
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // loop through all pixels
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                // get rgb values from intValues where each int holds the rgb values for a pixel.
                // if quantized, convert each rgb value to a byte, otherwise to a float

                imgData.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imgData.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imgData.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);

            }
        }
    }

}
