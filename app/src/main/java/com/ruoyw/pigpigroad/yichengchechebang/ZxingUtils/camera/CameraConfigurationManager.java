/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.Constant;

/**
 * Camera参数配置
 */
public final class CameraConfigurationManager {

	private static final String TAG = "CameraConfiguration";

	private final Context context;
	private Point screenResolution;
	private Point cameraResolution;
	private Point bestPreviewSize;

	CameraConfigurationManager(Context context) {
		this.context = context;
	}

	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
	void initFromCameraParameters(OpenCamera camera) {
		Camera.Parameters parameters = camera.getCamera().getParameters();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		Point theScreenResolution = new Point();
		display.getSize(theScreenResolution);
		screenResolution = theScreenResolution;
		Log.i(TAG, "Screen resolution in current orientation: " + screenResolution);
		cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
		Log.i(TAG, "Camera resolution: " + cameraResolution);
		bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
		Log.i(TAG, "Best available preview size: " + bestPreviewSize);
	}

	void setDesiredCameraParameters(OpenCamera camera, boolean safeMode) {

		Camera theCamera = camera.getCamera();
		Camera.Parameters parameters = theCamera.getParameters();

		if (parameters == null) {
			Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
			return;
		}

		Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

		if (safeMode) {
			Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		CameraConfigurationUtils.setFocus(
				parameters,
				prefs.getBoolean(Constant.KEY_AUTO_FOCUS, true),
				prefs.getBoolean(Constant.KEY_DISABLE_CONTINUOUS_FOCUS, true),
				safeMode);

		parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);

		theCamera.setParameters(parameters);
		//设置屏幕方向为垂直方向
		theCamera.setDisplayOrientation(90);
	}

	public Point getCameraResolution() {
		return cameraResolution;
	}

	Point getScreenResolution() {
		return screenResolution;
	}

	boolean getTorchState(Camera camera) {
		if (camera != null) {
			Camera.Parameters parameters = camera.getParameters();
			if (parameters != null) {
				String flashMode = parameters.getFlashMode();
				return flashMode != null &&
						(Camera.Parameters.FLASH_MODE_ON.equals(flashMode) ||
								Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
			}
		}
		return false;
	}

	void setTorch(Camera camera, boolean newSetting) {
		Camera.Parameters parameters = camera.getParameters();
		doSetTorch(parameters, newSetting, false);
		camera.setParameters(parameters);
	}

	private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
		CameraConfigurationUtils.setTorch(parameters, newSetting);
	}

}
