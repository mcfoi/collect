package com.geoodk.collect.android.widgets;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;
import org.javarosa.form.api.FormEntryPrompt;
import com.geoodk.collect.android.R;
import com.geoodk.collect.android.activities.FormEntryActivity;
import com.geoodk.collect.android.application.Collect;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Widget that allows user to launch PocketLAI for direct LAI measurement while filling the form.
 * 
 * @author Marco Foi (foimarco@gmail.com)
 */
public class LAIMeasureWidget extends QuestionWidget implements IBinaryWidget {
	
	private Button mMeasureLaiButton;
	private TextView mStringAnswer;

	public LAIMeasureWidget(Context context, FormEntryPrompt prompt) {
		super(context, prompt);
		setOrientation(LinearLayout.VERTICAL);

		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(7, 5, 7, 5);
		
		// set button formatting
		mMeasureLaiButton = new Button(getContext());
		mMeasureLaiButton.setId(QuestionWidget.newUniqueId());
		mMeasureLaiButton.setText(getContext().getString(R.string.measure_lai));
		mMeasureLaiButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
				mAnswerFontsize);
		mMeasureLaiButton.setPadding(20, 20, 20, 20);
		mMeasureLaiButton.setEnabled(!prompt.isReadOnly());
		mMeasureLaiButton.setLayoutParams(params);
		
		// launch LAI retrival intent on click
		mMeasureLaiButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Collect.getInstance()
						.getActivityLogger()
						.logInstanceAction(this, "measureLAI", "click",
								mPrompt.getIndex());
				Intent i = new Intent("eu.marcofoi.android.PocketLAI.ACTION_LAIMEASURE");
				try {
					Collect.getInstance().getFormController()
							.setIndexWaitingForData(mPrompt.getIndex());
					((Activity) getContext()).startActivityForResult(i,
							FormEntryActivity.LAI_MEASURE);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(
							getContext(),
							getContext().getString(
									R.string.lai_application_error),
							Toast.LENGTH_SHORT).show();
					Collect.getInstance().getFormController()
							.setIndexWaitingForData(null);
				}
			}
		});
		
		// set text formatting
		mStringAnswer = new TextView(getContext());
		mStringAnswer.setId(QuestionWidget.newUniqueId());
		mStringAnswer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
		mStringAnswer.setGravity(Gravity.CENTER);

		String s = prompt.getAnswerText();
		if (s != null) {
			mMeasureLaiButton.setText(getContext().getString(
					R.string.remeasure_lai));
			mStringAnswer.setText(s);
		}
		// finish complex layout
		addView(mMeasureLaiButton);
		addView(mStringAnswer);
	}
	

	@Override
	public void clearAnswer() {
		mStringAnswer.setText(null);
		mMeasureLaiButton.setText(getContext().getString(R.string.measure_lai));
	}
	
	@Override
	public IAnswerData getAnswer() {
		String s = mStringAnswer.getText().toString();
		if (s == null || s.equals("")) {
			return null;
		} else {
			return new StringData(s);
		}
	}

	/**
	 * Allows answer to be set externally in {@Link FormEntryActivity}.
	 */
	@Override
	public void setBinaryData(Object answer) {
		mStringAnswer.setText((String) answer);
		Collect.getInstance().getFormController().setIndexWaitingForData(null);
	}

	@Override
	public void setFocus(Context context) {
		// Hide the soft keyboard if it's showing.
		InputMethodManager inputManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
	}

	@Override
	public boolean isWaitingForBinaryData() {
		return mPrompt.getIndex().equals(
				Collect.getInstance().getFormController()
						.getIndexWaitingForData());
	}

	@Override
	public void cancelWaitingForBinaryData() {
		Collect.getInstance().getFormController().setIndexWaitingForData(null);
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		mStringAnswer.setOnLongClickListener(l);
		mMeasureLaiButton.setOnLongClickListener(l);
	}

	@Override
	public void cancelLongPress() {
		super.cancelLongPress();
		mMeasureLaiButton.cancelLongPress();
		mStringAnswer.cancelLongPress();
	}

}
