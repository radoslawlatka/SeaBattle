/*
 * Copyright (C) 2011 The Android Open Source Project
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
package pl.marzlat.viewww;

import pl.marzlat.R;
import android.app.Fragment;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BoardFragment extends Fragment {

	private GLSurfaceView glview;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.fragment_gameplay, container, false);
    	
/*    	glview = (GLSurfaceView) view.findViewById(R.id.myGLSurfaceView);
    	glview.setEGLContextClientVersion(2);
        glview.setRenderer(new MyGLRenderer());

        glview.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);*/


    	
		return view;
	}

	

    @Override
	public void onPause() {
    	super.onPause();
    }

    @Override
	public void onResume() {
        super.onResume();
    }
}