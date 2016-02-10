package sk.android.training.utils.listeners;

import android.os.Bundle;

public interface FragmentsActionsListener {

    void onFragmentAction(int action, Bundle bundle, OnFragmentFinishListener listener);
}
