/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.google.android.apps.location.gps.gnsslogger;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.apps.location.gps.gnsslogger.TimerService.*;

import static com.google.android.gms.common.internal.Preconditions.*;

//import static com.google.common.base.Preconditions.*;

/**
 * A {@link Dialog} allowing "Hours", "Minutes", and "Seconds" to be selected for a timer
 */
public class TimerFragment extends DialogFragment {
	private TimerListener mListener;

	@Override
	public void onAttach(Context context/*Activity activity*/) {
		super.onAttach(context/*activity*/);

		checkState(getTargetFragment() instanceof TimerListener, "Target fragment is not instance of TimerListener");

		mListener = (TimerListener) getTargetFragment();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View view = getActivity().getLayoutInflater().inflate(R.layout.timer, null);
		final NumberPicker timerHours = (NumberPicker) view.findViewById(R.id.hours_picker);
		final NumberPicker timerMinutes = (NumberPicker) view.findViewById(R.id.minutes_picker);
		final NumberPicker timerSeconds = (NumberPicker) view.findViewById(R.id.seconds_picker);

		final TimerValues values;

		if (getArguments() != null) {
			values = TimerValues.fromBundle(getArguments());
		} else {
			values = new TimerValues(0 /* hours */, 0 /* minutes */, 0 /* seconds */);
		}

		values.configureHours(timerHours);
		values.configureMinutes(timerMinutes);
		values.configureSeconds(timerSeconds);

		builder.setTitle(R.string.timer_title);
		builder.setView(view);
		builder.setPositiveButton(R.string.timer_set, (dialog, id) -> mListener.processTimerValues(new TimerValues(timerHours.getValue(), timerMinutes.getValue(), timerSeconds.getValue())));
		builder.setNeutralButton(R.string.timer_cancel, (dialog, id) -> mListener.processTimerValues(values));
		builder.setNegativeButton(R.string.timer_reset, (dialog, id) -> mListener.processTimerValues(new TimerValues(0 /* hours */, 0 /* minutes */, 0 /* seconds */)));

		return builder.create();
	}
}
