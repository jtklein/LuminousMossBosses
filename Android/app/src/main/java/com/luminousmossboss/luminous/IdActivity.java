package com.luminousmossboss.luminous;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.luminousmossboss.luminous.model.Observation;

/**
 * Created by andrey on 4/2/15.
 */
public class IdActivity extends AsyncTask <String, Void, Boolean>{
    private Context mContext;
    private Observation mobservation;
    private ObservationDBHandler db;
    private ProgressBar progressBar;
    private Button sendButton;
    private ObservationFragment startingFragment;


    public IdActivity(Context context, int observationId, ObservationFragment startingFragment)
    {
        this.startingFragment = startingFragment;
        if (startingFragment.getProgressBar() == null)
        this.progressBar = startingFragment.getProgressBar();
        this.sendButton = startingFragment.getSendButton();
        mContext = context;
        db = new ObservationDBHandler(mContext);
        mobservation = ObservationFactory.getObservation(observationId, context);

    }
    @Override
    protected void onPreExecute()
    {
        mobservation.setBeingProcessed(true);
        try {
            sendButton.setVisibility(View.GONE);
            sendButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean detectionResult = Util.detectImage(params[0], mContext);
        return detectionResult;
    }
    @Override
    protected void onPostExecute(Boolean detectionResult)
    {
        db.updateProcessed(mobservation.getId(), true);
        Toast message;
        mobservation.setBeingProcessed(false);
        mobservation.setHasBeenProcceced();
        progressBar = startingFragment.getProgressBar();
        sendButton = startingFragment.getSendButton();
        if (detectionResult)
        {
            message = Toast.makeText(mContext, R.string.id_silene_acaulis, Toast.LENGTH_LONG);
            mobservation.updateIsSilene(mContext);

        }
        else
        {
            message = Toast.makeText(mContext, R.string.id_unknown, Toast.LENGTH_LONG);


        }
        progressBar.setVisibility(View.GONE);
        sendButton.setEnabled(true);
        sendButton.setVisibility(View.VISIBLE);
        sendButton.setText("Send");
        db.close();
        message.show();



    }
}
