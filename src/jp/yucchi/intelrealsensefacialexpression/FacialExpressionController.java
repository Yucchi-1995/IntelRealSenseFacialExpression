package jp.yucchi.intelrealsensefacialexpression;

import intel.rssdk.PXCMCapture;
import intel.rssdk.PXCMFaceConfiguration;
import intel.rssdk.PXCMFaceData;
import intel.rssdk.PXCMFaceData.ExpressionsData;
import intel.rssdk.PXCMFaceData.LandmarksData;
import intel.rssdk.PXCMFaceData.PoseData;
import intel.rssdk.PXCMFaceData.PulseData;
import intel.rssdk.PXCMFaceModule;
import intel.rssdk.PXCMImage;
import intel.rssdk.PXCMRectI32;
import intel.rssdk.PXCMSenseManager;
import intel.rssdk.PXCMSession;
import intel.rssdk.pxcmStatus;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Yucchi
 */
public class FacialExpressionController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    // Eye brow Movement
    @FXML
    private Label browRaiserLeftLabel;
    @FXML
    private ProgressBar browRaiserLeftProgressBar;
    @FXML
    private Label browRaiserLeftResultLabel;
    @FXML
    private Label browRaiserRightLabel;
    @FXML
    private ProgressBar browRaiserRightProgressBar;
    @FXML
    private Label browRaiserRightResultLabel;
    @FXML
    private Label browLowererLeftLabel;
    @FXML
    private ProgressBar browLowererLeftProgressBar;
    @FXML
    private Label browLowererLeftResultLabel;
    @FXML
    private Label browLowererRightLabel;
    @FXML
    private ProgressBar browLowererRightProgressBar;
    @FXML
    private Label browLowererRightResultLabel;

    // Mouth Movement
    @FXML
    private Label smileLabel;
    @FXML
    private ProgressBar smileProgressBar;
    @FXML
    private Label smiletResultLabel;
    @FXML
    private Label kissLabel;
    @FXML
    private ProgressBar kissProgressBar;
    @FXML
    private Label kissResultLabel;
    @FXML
    private Label mouthOpenLabel;
    @FXML
    private ProgressBar mouthOpenProgressBar;
    @FXML
    private Label mouthOpenResultLabel;
    @FXML
    private Label tongueOutLabel;
    @FXML
    private ProgressBar tongueOutProgressBar;
    @FXML
    private Label tongueOutResultLabel;

    // Eye Movement
    @FXML
    private Label eyesClosedLeftLabel;
    @FXML
    private ProgressBar eyesClosedLeftProgressBar;
    @FXML
    private Label eyesClosedLeftResultLabel;
    @FXML
    private Label eyesClosedRightLabel;
    @FXML
    private ProgressBar eyesClosedRightProgressBar;
    @FXML
    private Label eyesClosedRightResultLabel;
    @FXML
    private Label eyesTurnLeftLabel;
    @FXML
    private ProgressBar eyesTurnLeftProgressBar;
    @FXML
    private Label eyesTurnLeftResultLabel;
    @FXML
    private Label eyesTurnRightLabel;
    @FXML
    private ProgressBar eyesTurnRightProgressBar;
    @FXML
    private Label eyesTurnRightResultLabel;
    @FXML
    private Label eyesUpLabel;
    @FXML
    private ProgressBar eyesUpProgressBar;
    @FXML
    private Label eyesUpResultLabel;
    @FXML
    private Label eyesDownLabel;
    @FXML
    private ProgressBar eyesDownProgressBar;
    @FXML
    private Label eyesDownResultLabel;

    // Mouth brow Movement
    @FXML
    private Label puffLeftLabel;
    @FXML
    private ProgressBar puffLeftProgressBar;
    @FXML
    private Label puffLeftResultLabel;
    @FXML
    private Label puffRightLabel;
    @FXML
    private ProgressBar puffRightProgressBar;
    @FXML
    private Label puffRightResultLabel;

    // Heart Rate
    @FXML
    private Label heartRatetLabel;
    @FXML
    private ProgressBar heartRateProgressBar;
    @FXML
    private Label heartRateResultLabel;

    // Pose
    @FXML
    private Label yawLabel;
    @FXML
    private Label pitchLabel;
    @FXML
    private Label rollLabel;

    @FXML
    private CheckBox detectionCheckBox;

    @FXML
    private CheckBox landmarkCheckBox;

    @FXML
    private ImageView imageView;

    @FXML
    private Label recognitionLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button UnregistreButton;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button exitButton;

    private PXCMSenseManager senseManager;
    private StreamService streamService;
    private pxcmStatus pxcmStatus;
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private Image image;
    private final StringProperty errorContent = new SimpleStringProperty();
    private PXCMFaceData faceData;
    private PXCMFaceData.RecognitionData registeredData;

    private static final int DETECTION_MAX_FACES = 2;
    private static final int POSE_MAX_FACES = 1;
    private static final int LANDMARK_MAX_FACES = 1;
    private static final int EXPRESSION_MAX_FACES = 1;
    private static final int PULSE_MAX_FACES = 1;
    private static final int RECOGNITION_MAX_FACES = 2;
    private int id;

    private GraphicsContext gc;
    private PXCMSession.ImplVersion version;
    private final ConcurrentHashMap<Integer, String> registeredNameMap = new ConcurrentHashMap<>(RECOGNITION_MAX_FACES);

    private final BooleanProperty rgist = new SimpleBooleanProperty();
    private final BooleanProperty unrgist = new SimpleBooleanProperty();

    @FXML
    private void handleStartButtonAction(ActionEvent event) {

        streamService = new StreamService();

        streamService.setOnSucceeded(wse -> {
            if (streamService != null && streamService.getValue() != null) {
                imageView.setImage(image);
                streamService.restart();
            }
        });

        streamService.setOnFailed(wse -> {
            if (errorContent.getValue() == null) {
                errorContent.setValue("Error!\n" + "StreamService Failed.");
            }
            errorProcessing();
        });

        startButton.disableProperty().bind(streamService.runningProperty());
        stopButton.disableProperty().bind(streamService.runningProperty().not());

        registerButton.disableProperty().bind(streamService.runningProperty().not().or(rgist));
        UnregistreButton.disableProperty().bind(streamService.runningProperty().not().or(unrgist));

        // カメラ初期化
        initCamera();

        if (streamService != null && !streamService.isRunning()) {
            streamService.reset();
            streamService.start();
        }

    }

    @FXML
    private void handleStoptButtonAction(ActionEvent event) {

        if (streamService != null) {
            streamService.cancel();
            streamService = null;
        }

        if (faceData != null) {
            faceData.close();
            faceData = null;
        }

        if (senseManager != null) {
            senseManager.Close();
            senseManager = null;
        }

        registeredNameMap.clear();

        gc.clearRect(0, 0, WIDTH, HEIGHT);

        yawLabel.setText("Yaw: --");
        rollLabel.setText("Roll: --");
        pitchLabel.setText("Pitch: --");

        browRaiserLeftProgressBar.setProgress(0 / 100.0);
        browRaiserLeftResultLabel.setText("--");
        browRaiserRightProgressBar.setProgress(0 / 100.0);
        browRaiserRightResultLabel.setText("--");
        browLowererLeftProgressBar.setProgress(0 / 100.0);
        browLowererLeftResultLabel.setText("--");
        browLowererRightProgressBar.setProgress(0 / 100.0);
        browLowererRightResultLabel.setText("--");
        smileProgressBar.setProgress(0 / 100.0);
        smiletResultLabel.setText("--");
        kissProgressBar.setProgress(0 / 100.0);
        kissResultLabel.setText("--");
        mouthOpenProgressBar.setProgress(0 / 100.0);
        mouthOpenResultLabel.setText("--");
        tongueOutProgressBar.setProgress(0 / 100.0);
        tongueOutResultLabel.setText("--");
        eyesClosedLeftProgressBar.setProgress(0 / 100.0);
        eyesClosedLeftResultLabel.setText("--");
        eyesClosedRightProgressBar.setProgress(0 / 100.0);
        eyesClosedRightResultLabel.setText("--");
        eyesTurnLeftProgressBar.setProgress(0 / 100.0);
        eyesTurnLeftResultLabel.setText("--");
        eyesTurnRightProgressBar.setProgress(0 / 100.0);
        eyesTurnRightResultLabel.setText("--");
        eyesUpProgressBar.setProgress(0 / 100.0);
        eyesUpResultLabel.setText("--");
        eyesDownProgressBar.setProgress(0 / 100.0);
        eyesDownResultLabel.setText("--");
        puffLeftProgressBar.setProgress(0 / 100.0);
        puffLeftResultLabel.setText("--");
        puffRightProgressBar.setProgress(0 / 100.0);
        puffRightResultLabel.setText("--");
        heartRateProgressBar.setProgress(0 / 100.0);
        heartRateResultLabel.setText("--");
        recognitionLabel.setText("Intel Realsense " + "SDK Version " + version.major + "." + version.minor);

        imageView.setImage(new Image(this.getClass().getResourceAsStream("resources/duke_cake.jpg")));

    }

    @FXML
    private void handleExittButtonAction(ActionEvent event) {
        exitProcessing();
    }

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        // 顔を登録
        if (registeredData != null) {
            if (!registeredData.IsRegistered() && registeredNameMap.size() < RECOGNITION_MAX_FACES) {
                TextInputDialog dialog = new TextInputDialog("");
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(dialog.getEditor().textProperty().isEmpty().or(rgist));
                dialog.setTitle("Face Recognition");
                dialog.setHeaderText("Face Recognition");
                dialog.setContentText("Please enter your name ");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    id = registeredData.RegisterUser();
                    registeredNameMap.putIfAbsent(id, name);
                });

            } else if (registeredData.IsRegistered()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                shakeAlert(alert);
                alert.initStyle(StageStyle.TRANSPARENT);
                alert.setTitle("WARNING!");
                alert.setHeaderText("WARNING!");
                alert.setContentText("It is already registered.");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            } else if (registeredNameMap.size() >= RECOGNITION_MAX_FACES) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                shakeAlert(alert);
                alert.initStyle(StageStyle.TRANSPARENT);
                alert.setTitle("WARNING!");
                alert.setHeaderText("WARNING!");
                alert.setContentText("Cannot register any more. ");
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
            }
        }
    }

    @FXML
    private void handleUnregisterButtonAction(ActionEvent event) {

        // 顔を登録解除
        if (registeredData.IsRegistered()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("CONFIRMATION");
            alert.setContentText("Do you really delete registration? ");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        if (registeredNameMap.containsKey(id)) {
                            registeredNameMap.remove(id);
                        }
                        registeredData.UnregisterUser();
                    });
            event.consume();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        imageView.setImage(new Image(this.getClass().getResourceAsStream("resources/duke_cake.jpg")));

        gc = canvas.getGraphicsContext2D();

        // SDK Version 表示
        try (PXCMSession session = PXCMSession.CreateInstance()) {
            version = session.QueryVersion();
        }

        recognitionLabel.setText("Intel Realsense " + "SDK Version " + version.major + "." + version.minor);

        // Intel RealSense SDK のバグのためチェックボックスを使ったお遊びです(^_^;
        landmarkCheckBox.selectedProperty().addListener((ov, oldVal, newVal) -> {
            if (newVal != null && landmarkCheckBox.isSelected()) {
                if (version.major <= 7) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    shakeAlert(alert);
                    TextArea textArea = new TextArea("Landmark cannot be used in SDK of this version. \n"
                            + "Intel Realsense " + version);
                    textArea.setEditable(false);
                    alert.getDialogPane().setExpandableContent(textArea);
                    alert.initStyle(StageStyle.TRANSPARENT);
                    alert.setTitle("WARNING!");
                    alert.setHeaderText("WARNING!");
                    alert.setContentText("Landmark cannot be used in SDK of this version. ");
                    alert.showAndWait()
                            .filter(response -> response == ButtonType.OK)
                            .ifPresent(response -> landmarkCheckBox.setSelected(false));
                }
            }
        });
    }

    private void initCamera() {

        // SenseManagerを生成
        senseManager = PXCMSenseManager.CreateInstance();
        // カラーストリームを有効にする
        senseManager.EnableStream(PXCMCapture.StreamType.STREAM_TYPE_COLOR, WIDTH, HEIGHT);

        // faceConfigを有効にする
        senseManager.EnableFace(null);
        //顔検出器を生成
        PXCMFaceModule faceModule = senseManager.QueryFace();
        //顔検出のプロパティを取得
        PXCMFaceConfiguration faceConfig = faceModule.CreateActiveConfiguration();
        // フェイストラッキングモードで Depth モードも使用できるようにしておく。
        faceConfig.SetTrackingMode(PXCMFaceConfiguration.TrackingModeType.FACE_MODE_COLOR_PLUS_DEPTH);
        // 一般的なカラーモードだけの WebCam の場合はフェイストラッキングモードの設定を下記のようにする
//        faceConfig.SetTrackingMode(PXCMFaceConfiguration.TrackingModeType.FACE_MODE_COLOR);
        // Set to enable all alerts
        faceConfig.EnableAllAlerts();
        faceConfig.ApplyChanges();

        // PXCM_STATUS 初期化
        pxcmStatus = senseManager.Init();

        // ミラーモードにする
//        senseManager.QueryCaptureManager().QueryDevice().SetMirrorMode(PXCMCapture.Device.MirrorMode.MIRROR_MODE_HORIZONTAL);
        // デバイス情報の取得
        PXCMCapture.Device device = senseManager.QueryCaptureManager().QueryDevice();
        // Intel RealSense Camera F200 なら細かな設定をする。
        PXCMCapture.DeviceInfo deviceInfo = new PXCMCapture.DeviceInfo();
        device.QueryDeviceInfo(deviceInfo);
        if (deviceInfo.model == PXCMCapture.DeviceModel.DEVICE_MODEL_F200) {
            device.SetDepthConfidenceThreshold(1);
            device.SetIVCAMFilterOption(6);
            device.SetIVCAMMotionRangeTradeOff(21);
        }

        // 顔識別のプロパティを取得
        PXCMFaceConfiguration.RecognitionConfiguration recognitionConfiguration = faceConfig.QueryRecognition();
        // 顔識別を有効にする
        recognitionConfiguration.Enable();
        // 顔識別用データベース用インスタンス生成
        PXCMFaceConfiguration.RecognitionConfiguration.RecognitionStorageDesc desc = new PXCMFaceConfiguration.RecognitionConfiguration.RecognitionStorageDesc();
        // 識別可能な顔の最大数
        desc.maxUsers = RECOGNITION_MAX_FACES;
        // 顔識別用データベースを"MyDB"という名前で作成
        recognitionConfiguration.CreateStorage("MyDB", desc);
        // 顔識別用データベース初期化
        recognitionConfiguration.UseStorage("MyDB");
        // 顔識別の登録モードを設定
        recognitionConfiguration.SetRegistrationMode(PXCMFaceConfiguration.RecognitionConfiguration.RecognitionRegistrationMode.REGISTRATION_MODE_CONTINUOUS);

        // Face Detection 有効
        faceConfig.detection.isEnabled = true;
        // スムージングレベル デフォルトは SMOOTHING_DISABLED
//        faceConfig.detection.smoothingLevel = PXCMFaceConfiguration.SmoothingLevelType.SMOOTHING_MEDIUM;
        // Landmark 有効
        faceConfig.landmarks.isEnabled = true;
        // スムージングレベル デフォルトは SMOOTHING_DISABLED
//        faceConfig.landmarks.smoothingLevel = PXCMFaceConfiguration.SmoothingLevelType.SMOOTHING_MEDIUM;
        // Pose 有効
        faceConfig.pose.isEnabled = true;
        // スムージングレベル デフォルトは SMOOTHING_DISABLED
//        faceConfig.pose.smoothingLevel = PXCMFaceConfiguration.SmoothingLevelType.SMOOTHING_HIGH;
        // Expression 有効
        faceConfig.QueryExpressions().Enable();
        // Expression すべて有効
        faceConfig.QueryExpressions().EnableAllExpressions();
        // Pulse 有効
        faceConfig.QueryPulse().Enable();
        // 顔検出の最大可能人数の設定
        faceConfig.detection.maxTrackedFaces = DETECTION_MAX_FACES;
        // 顔の姿勢情報を取得する最大可能人数の設定
        faceConfig.pose.maxTrackedFaces = POSE_MAX_FACES;
        // ランドマークを取得する最大可能人数の設定
        faceConfig.landmarks.maxTrackedFaces = LANDMARK_MAX_FACES;
        // Expression 取得する最大可能人数の設定
        faceConfig.QueryExpressions().properties.maxTrackedFaces = EXPRESSION_MAX_FACES;
        // Pulse 取得する最大可能人数の設定
        faceConfig.QueryPulse().properties.maxTrackedFaces = PULSE_MAX_FACES;
        // TrackingStrategyType デフォルトは STRATEGY_CLOSEST_TO_FARTHES (Depth 使用)
//        faceConfig.strategy = PXCMFaceConfiguration.TrackingStrategyType.STRATEGY_APPEARANCE_TIME;
        faceConfig.ApplyChanges();
        faceConfig.Update();

        //SenceManager 顔モジュール
        faceData = faceModule.CreateOutput();

    }

    class StreamService extends Service<Image> {

        // Expression データ
        private float browRaiserLeft;
        private float browRaiserRight;
        private float browLowererLeft;
        private float browLowererRight;
        private float smile;
        private float kiss;
        private float mouthOpen;
        private float tongueOut;
        private float eyesClosedLeft;
        private float eyesClosedRight;
        private float eyesTurnLeft;
        private float eyesTurnRight;
        private float eyesUp;
        private float eyesDown;
        private float puffLeft;
        private float puffRight;

        @Override
        protected Task<Image> createTask() {
            Task<Image> task = new Task<Image>() {
                // Detection データ
                private double[] detectionX;
                private double[] detectionY;
                private double[] detectionW;
                private double[] detectionH;
                // Pose データ
                private String yaw = "Yaw: --";
                private String roll = "Roll: --";
                private String pitch = "Pitch: --";
                // Pulse データ
                private int heartRate;

                private PXCMFaceData.LandmarksData[] landmarkData;
                private PXCMFaceData.ExpressionsData[] expressionData;
                private PXCMFaceData.PulseData[] pulseData;
                private PXCMFaceData.PoseData[] poseData;
                private int nFaces;
                private String searchResult;

                @Override
                protected Image call() throws Exception {

                    if (pxcmStatus == pxcmStatus.PXCM_STATUS_NO_ERROR) {

                        // フレーム取得
                        if (senseManager.AcquireFrame(true).isSuccessful()) {

                            // フレームデータ取得
                            PXCMCapture.Sample sample = senseManager.QuerySample();

                            if (sample.color != null) {
                                // データ取得
                                PXCMImage.ImageData cData = new PXCMImage.ImageData();
                                // アクセス権を取得（アクセス権の種類、画像フォーマット、データ）
                                pxcmStatus = sample.color.AcquireAccess(PXCMImage.Access.ACCESS_READ, PXCMImage.PixelFormat.PIXEL_FORMAT_RGB32, cData);

                                if (pxcmStatus.compareTo(pxcmStatus.PXCM_STATUS_NO_ERROR) < 0) {
                                    errorContent.setValue("Error!\n" + "Failed to AcquireAccess of ColorImage Data.");
                                    throw new Exception();
                                }

                                // BufferedImage に変換 １ピクセルあたり４バイトに注意、PXCMImage.PixelFormat.PIXEL_FORMAT_RGB24 だと３バイト
                                int cBuff[] = new int[cData.pitches[0] / 4 * HEIGHT];
                                cData.ToIntArray(0, cBuff);
                                BufferedImage bImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                                bImage.setRGB(0, 0, WIDTH, HEIGHT, cBuff, 0, cData.pitches[0] / 4);

                                // ImageView にセットできるように Image に変換
                                image = SwingFXUtils.toFXImage(bImage, null);

                                // データを解放
                                pxcmStatus = sample.color.ReleaseAccess(cData);

                                if (pxcmStatus.compareTo(pxcmStatus.PXCM_STATUS_NO_ERROR) < 0) {
                                    errorContent.setValue("Error!\n" + "Failed to ReleaseAccess of ColorImage Data.");
                                    throw new Exception();
                                }
                            }

                            // FaceProcessing Start ////////////////////////////
                            // 顔データを更新
                            faceData.Update();

                            // 検出された顔の数 
                            nFaces = faceData.QueryNumberOfDetectedFaces();

                            // Detection データ用
                            detectionX = new double[DETECTION_MAX_FACES];
                            detectionY = new double[DETECTION_MAX_FACES];
                            detectionW = new double[DETECTION_MAX_FACES];
                            detectionH = new double[DETECTION_MAX_FACES];

                            // LandmarkProcessing 用
                            // 検出された顔のランドマークデータをいれる配列       
                            landmarkData = new PXCMFaceData.LandmarksData[LANDMARK_MAX_FACES];

                            // 検出された顔の Expression データを入れる配列
                            expressionData = new PXCMFaceData.ExpressionsData[EXPRESSION_MAX_FACES];

                            // 検出された顔の Pulse データを入れる配列
                            pulseData = new PXCMFaceData.PulseData[PULSE_MAX_FACES];

                            // 検出された顔の pose データを入れる配列
                            poseData = new PXCMFaceData.PoseData[POSE_MAX_FACES];

                            // クリア
                            Platform.runLater(() -> {
                                // Canvas クリア
                                gc.clearRect(0, 0, WIDTH, HEIGHT);
                                // データ表示用画面クリア
                                if (nFaces < 1) {
                                    yaw = "Yaw: --";
                                    roll = "Roll: --";
                                    pitch = "Pitch: --";
                                    yawLabel.setText(yaw);
                                    rollLabel.setText(roll);
                                    pitchLabel.setText(pitch);

                                    browRaiserLeft = 0.0f;
                                    browRaiserRight = 0.0f;
                                    browLowererLeft = 0.0f;
                                    browLowererRight = 0.0f;
                                    smile = 0.0f;
                                    kiss = 0.0f;
                                    mouthOpen = 0.0f;
                                    tongueOut = 0.0f;
                                    eyesClosedLeft = 0.0f;
                                    eyesClosedRight = 0.0f;
                                    eyesTurnLeft = 0.0f;
                                    eyesTurnRight = 0.0f;
                                    eyesUp = 0.0f;
                                    eyesDown = 0.0f;
                                    puffLeft = 0.0f;
                                    puffRight = 0.0f;

                                    browRaiserLeftProgressBar.setProgress(0 / 100.0);
                                    browRaiserLeftResultLabel.setText("--");
                                    browRaiserRightProgressBar.setProgress(0 / 100.0);
                                    browRaiserRightResultLabel.setText("--");
                                    browLowererLeftProgressBar.setProgress(0 / 100.0);
                                    browLowererLeftResultLabel.setText("--");
                                    browLowererRightProgressBar.setProgress(0 / 100.0);
                                    browLowererRightResultLabel.setText("--");
                                    smileProgressBar.setProgress(0 / 100.0);
                                    smiletResultLabel.setText("--");
                                    kissProgressBar.setProgress(0 / 100.0);
                                    kissResultLabel.setText("--");
                                    mouthOpenProgressBar.setProgress(0 / 100.0);
                                    mouthOpenResultLabel.setText("--");
                                    tongueOutProgressBar.setProgress(0 / 100.0);
                                    tongueOutResultLabel.setText("--");
                                    eyesClosedLeftProgressBar.setProgress(0 / 100.0);
                                    eyesClosedLeftResultLabel.setText("--");
                                    eyesClosedRightProgressBar.setProgress(0 / 100.0);
                                    eyesClosedRightResultLabel.setText("--");
                                    eyesTurnLeftProgressBar.setProgress(0 / 100.0);
                                    eyesTurnLeftResultLabel.setText("--");
                                    eyesTurnRightProgressBar.setProgress(0 / 100.0);
                                    eyesTurnRightResultLabel.setText("--");
                                    eyesUpProgressBar.setProgress(0 / 100.0);
                                    eyesUpResultLabel.setText("--");
                                    eyesDownProgressBar.setProgress(0 / 100.0);
                                    eyesDownResultLabel.setText("--");
                                    puffLeftProgressBar.setProgress(0 / 100.0);
                                    puffLeftResultLabel.setText("--");
                                    puffRightProgressBar.setProgress(0 / 100.0);
                                    puffRightResultLabel.setText("--");
                                    heartRateProgressBar.setProgress(0 / 100.0);
                                    heartRateResultLabel.setText("--");
                                    recognitionLabel.setText("Face can not be detected.");
                                }
                            });

                            // REGISTER 顔登録ダイアログボタン制御用
                            if (nFaces < 1) {
                                rgist.setValue(Boolean.TRUE);
                            } else {
                                rgist.setValue(Boolean.FALSE);
                            }

                            // 検出された顔の数だけ繰り返し処理
                            for (int i = 0; i < nFaces; i++) {

                                if (faceData == null) {
                                    break;
                                }

                                // 顔データを取得
                                PXCMFaceData.Face face = faceData.QueryFaceByIndex(i);

                                // DetectionProcessing
                                // 顔データから Detection データ取得
                                if (i < DETECTION_MAX_FACES) {
                                    PXCMFaceData.DetectionData detectData = face.QueryDetection();

                                    if (detectData != null) {
                                        // 顔の領域情報インスタンス生成
                                        PXCMRectI32 rect = new PXCMRectI32();
                                        // 顔の領域情報を PXCMRectI32 rect に入れる、失敗したら false を返す
                                        if (detectData.QueryBoundingRect(rect)) {
                                            // 顔の領域情報を取り出す
                                            detectionX[i] = rect.x;
                                            detectionY[i] = rect.y;
                                            detectionW[i] = rect.w;
                                            detectionH[i] = rect.h;
                                        } else {
                                            detectionX[i] = -1.0;
                                            detectionY[i] = -1.0;
                                            detectionW[i] = -1.0;
                                            detectionH[i] = -1.0;
                                        }
                                    } else {
                                        detectionX[i] = -1.0;
                                        detectionY[i] = -1.0;
                                        detectionW[i] = -1.0;
                                        detectionH[i] = -1.0;
                                    }
                                }

                                // PoseProcessing
                                // Pose データ
                                if (i < POSE_MAX_FACES) {
                                    poseData[i] = face.QueryPose();
                                }

                                // LandmarkProcessing
                                // Landmark データ
                                if (i < LANDMARK_MAX_FACES) {
                                    landmarkData[i] = face.QueryLandmarks();
                                }

                                // ExpressionProcessing
                                // Expression データ
                                if (i < EXPRESSION_MAX_FACES) {
                                    expressionData[i] = face.QueryExpressions();
                                }

                                // PulseProcessing
                                // Pulse データ
                                if (i < PULSE_MAX_FACES) {
                                    pulseData[i] = face.QueryPulse();
                                }

                                // RecognitionProcessing
                                // 顔識別データ
                                registeredData = face.QueryRecognition();
                                if (registeredData != null) {
                                    // 識別した顔かチェック
                                    id = registeredData.QueryUserID();
                                    if (id >= 0) {
                                        // UNREGISTER ボタン制御用
                                        unrgist.setValue(Boolean.FALSE);
                                        // 顔識別データ取得
                                        if (registeredNameMap.containsKey(id)) {
                                            searchResult = registeredNameMap.getOrDefault(id, "Recognition: NO DATA!");
                                        }
                                        Platform.runLater(() -> {
                                            // 顔識別データ表示
                                            recognitionLabel.setText(searchResult);
                                        });
                                    } else {
                                        // UNREGISTER ボタン制御用
                                        unrgist.setValue(Boolean.TRUE);
                                        Platform.runLater(() -> {
                                            recognitionLabel.setText("Recognition: NO DATA");
                                        });
                                    }

                                }

                            }

                            // 検出された顔の詳細データ表示
                            Platform.runLater(() -> {
                                if (detectionCheckBox.isSelected()) {
                                    drawDetectRect();
                                }
                                if (landmarkCheckBox.isSelected()) {
                                    drawLandmark(landmarkData);
                                }
                                updatePose(poseData);
                                updateExpression(expressionData);
                                updateHeartRate(pulseData);
                            });

                            // FaceProcessing End ////////////////////////////
                            // 次のフレームデータを呼び出すためにフレームを解放する
                            senseManager.ReleaseFrame();

                        } else {
                            // 極まれにフレーム取得失敗する
//                            errorContent.setValue("Failed to acquire frame.");
//                            errorProcessing();
                            System.out.println("Failed to acquire frame.");
                        }

                    } else {
                        errorContent.setValue("Error!\n" + "Failed to Initialize.");
                        errorProcessing();
                    }

                    return image;
                }

                private void drawDetectRect() {

                    gc.setStroke(Color.BLUE);
                    gc.setLineWidth(3);

                    for (int i = 0; i < detectionX.length; i++) {
                        gc.strokeRoundRect(detectionX[i], detectionY[i],
                                detectionW[i], detectionH[i], 10, 10);
                    }

                }

                private void updatePose(PoseData[] poseData) {

                    // 一人しか検出できないように設定してあるので意味のないループ
                    for (PoseData poseData1 : poseData) {
                        if (poseData1 != null) {
                            PXCMFaceData.PoseEulerAngles pea = new PXCMFaceData.PoseEulerAngles();
                            poseData1.QueryPoseAngles(pea);
                            yaw = "Yaw: " + (int) pea.yaw;
                            roll = "Roll: " + (int) pea.roll;
                            pitch = "Pitch: " + (int) pea.pitch;
                        } else {
                            yaw = "Yaw: --";
                            roll = "Roll: --";
                            pitch = "Pitch: --";
                        }
                        yawLabel.setText(yaw);
                        rollLabel.setText(roll);
                        pitchLabel.setText(pitch);
                    }

                }

                private void updateExpression(ExpressionsData[] expressionData) {

                    // 一人しか検出できないように設定してあるので意味のないループ
                    for (ExpressionsData expressionData1 : expressionData) {
                        if (expressionData1 != null) {
                            PXCMFaceData.ExpressionsData.FaceExpressionResult score = new PXCMFaceData.ExpressionsData.FaceExpressionResult();
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_BROW_RAISER_LEFT, score)) {
                                browRaiserLeft = browRaiserLeft * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_BROW_RAISER_RIGHT, score)) {
                                browRaiserRight = browRaiserRight * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_BROW_LOWERER_LEFT, score)) {
                                browLowererLeft = browLowererLeft * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_BROW_LOWERER_RIGHT, score)) {
                                browLowererRight = browLowererRight * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_SMILE, score)) {
                                smile = smile * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_KISS, score)) {
                                kiss = kiss * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_MOUTH_OPEN, score)) {
                                mouthOpen = mouthOpen * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_TONGUE_OUT, score)) {
                                tongueOut = tongueOut * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_EYES_CLOSED_LEFT, score)) {
                                eyesClosedLeft = eyesClosedLeft * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_EYES_CLOSED_RIGHT, score)) {
                                eyesClosedRight = eyesClosedRight * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_EYES_TURN_LEFT, score)) {
                                eyesTurnLeft = eyesTurnLeft * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_EYES_TURN_RIGHT, score)) {
                                eyesTurnRight = eyesTurnRight * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_EYES_UP, score)) {
                                eyesUp = eyesUp * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_EYES_DOWN, score)) {
                                eyesDown = eyesDown * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_PUFF_LEFT, score)) {
                                puffLeft = puffLeft * 0.9f + score.intensity * 0.101f;
                            }
                            if (expressionData1.QueryExpression(PXCMFaceData.ExpressionsData.FaceExpression.EXPRESSION_PUFF_RIGHT, score)) {
                                puffRight = puffRight * 0.9f + score.intensity * 0.101f;
                            }
                            browRaiserLeftProgressBar.setProgress((int) browRaiserLeft / 100.0);
                            browRaiserLeftResultLabel.setText(String.valueOf((int) browRaiserLeft));
                            browRaiserRightProgressBar.setProgress((int) browRaiserRight / 100.0);
                            browRaiserRightResultLabel.setText(String.valueOf((int) browRaiserRight));
                            browLowererLeftProgressBar.setProgress((int) browLowererLeft / 100.0);
                            browLowererLeftResultLabel.setText(String.valueOf((int) browLowererLeft));
                            browLowererRightProgressBar.setProgress((int) browLowererRight / 100.0);
                            browLowererRightResultLabel.setText(String.valueOf((int) browLowererRight));
                            smileProgressBar.setProgress((int) smile / 100.0);
                            smiletResultLabel.setText(String.valueOf((int) smile));
                            kissProgressBar.setProgress((int) kiss / 100.0);
                            kissResultLabel.setText(String.valueOf((int) kiss));
                            mouthOpenProgressBar.setProgress((int) mouthOpen / 100.0);
                            mouthOpenResultLabel.setText(String.valueOf((int) mouthOpen));
                            tongueOutProgressBar.setProgress((int) tongueOut / 100.0);
                            tongueOutResultLabel.setText(String.valueOf((int) tongueOut));
                            eyesClosedLeftProgressBar.setProgress((int) eyesClosedLeft / 100.0);
                            eyesClosedLeftResultLabel.setText(String.valueOf((int) eyesClosedLeft));
                            eyesClosedRightProgressBar.setProgress((int) eyesClosedRight / 100.0);
                            eyesClosedRightResultLabel.setText(String.valueOf((int) eyesClosedRight));
                            eyesTurnLeftProgressBar.setProgress((int) eyesTurnLeft / 100.0);
                            eyesTurnLeftResultLabel.setText(String.valueOf((int) eyesTurnLeft));
                            eyesTurnRightProgressBar.setProgress((int) eyesTurnRight / 100.0);
                            eyesTurnRightResultLabel.setText(String.valueOf((int) eyesTurnRight));
                            eyesUpProgressBar.setProgress((int) eyesUp / 100.0);
                            eyesUpResultLabel.setText(String.valueOf((int) eyesUp));
                            eyesDownProgressBar.setProgress((int) eyesDown / 100.0);
                            eyesDownResultLabel.setText(String.valueOf((int) eyesDown));
                            puffLeftProgressBar.setProgress((int) puffLeft / 100.0);
                            puffLeftResultLabel.setText(String.valueOf((int) puffLeft));
                            puffRightProgressBar.setProgress((int) puffRight / 100.0);
                            puffRightResultLabel.setText(String.valueOf((int) puffRight));
                        } else {
//                            System.out.println("expressionData is null.");
                        }
                    }

                }

                private void updateHeartRate(PulseData[] pulseData) {
                    // 一人しか検出できないように設定してあるので意味のないループ
                    for (PulseData pulseData1 : pulseData) {
                        if (pulseData1 != null) {
                            heartRate = (int) pulseData1.QueryHeartRate();
                            heartRateProgressBar.setProgress(heartRate / 100.0);
                            heartRateResultLabel.setText(String.valueOf(heartRate));
                        } else {
//                    System.out.println("pulseData is null.");
                        }
                    }

                }

                private void drawLandmark(LandmarksData[] landmarkData) {

                    gc.setFill(Color.RED);

                    for (LandmarksData landmarkData1 : landmarkData) {
                        if (landmarkData1 != null) {
                            // ランドマーク特徴点数を取得
                            int numPoints = landmarkData1.QueryNumPoints();
                            // ランドマーク特徴点データを入れる配列のインスタンス生成
                            PXCMFaceData.LandmarkPoint[] landmarkPoints = new PXCMFaceData.LandmarkPoint[numPoints];
                            //ランドマークデータから、特徴点の位置を取得、失敗したら false を返す
                            if (landmarkData1.QueryPoints(landmarkPoints)) {
                                for (PXCMFaceData.LandmarkPoint landmarkPoint : landmarkPoints) {
                                    if (landmarkPoint != null) {
                                        gc.fillOval(landmarkPoint.image.x, landmarkPoint.image.y, 3, 3);
                                        System.out.println(landmarkPoint.image.x + " : " + landmarkPoint.image.y);
                                    } else {
//                                        System.out.println("landmarkPoints is null.");
                                    }
                                }
                            }

                        } else {
                            System.out.println("landmarkData is null.");
                        }
                    }

                }

            };

            return task;
        }

    }

    private void shakeAlert(Alert alertDialog) {

        Timeline shakeTimeline = new Timeline(new KeyFrame(Duration.seconds(0.03), new EventHandler<ActionEvent>() {

            boolean state = true;
            private final int SHAKE_DISTANCE = 90;
            int modulation = 2;

            @Override
            public void handle(ActionEvent ae) {
                if (state) {
                    alertDialog.setX(alertDialog.getX() + SHAKE_DISTANCE / modulation++);
                } else {
                    alertDialog.setX(alertDialog.getX() - SHAKE_DISTANCE / modulation++);
                }
                state = !state;
            }
        }));

        shakeTimeline.setCycleCount(16);
        shakeTimeline.setAutoReverse(false);
        shakeTimeline.play();

    }

    private void errorProcessing() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        shakeAlert(alert);
        TextArea textArea = new TextArea(errorContent.get());
        textArea.setEditable(false);
        alert.getDialogPane().setExpandableContent(textArea);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setTitle("ERROR");
        alert.setHeaderText("Error!\n"
                + "An unexpected error has occurred.");
        alert.setContentText("Exit the application.");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> exitProcessing());
    }

    private void exitProcessing() {

        // クロージングアニメーション
        DoubleProperty closeOpacityProperty = new SimpleDoubleProperty(1.0);
        anchorPane.getScene().getWindow().opacityProperty().bind(closeOpacityProperty);

        Timeline closeTimeline = new Timeline(
                new KeyFrame(
                        new Duration(100),
                        new KeyValue(closeOpacityProperty, 1.0)
                ), new KeyFrame(
                        new Duration(2_500),
                        new KeyValue(closeOpacityProperty, 0.0)
                ));

        EventHandler<ActionEvent> eh = ae -> {

            if (streamService != null && streamService.isRunning()) {
                streamService.cancel();
                streamService = null;
            }

            if (faceData != null) {
                faceData.close();
                faceData = null;
            }

            if (senseManager != null) {
                senseManager.Close();
                senseManager = null;
            }

            Platform.exit();
            System.exit(0);

        };

        closeTimeline.setOnFinished(eh);
        closeTimeline.setCycleCount(1);
        closeTimeline.play();
    }

}
