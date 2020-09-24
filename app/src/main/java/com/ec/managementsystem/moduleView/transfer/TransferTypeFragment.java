package com.ec.managementsystem.moduleView.transfer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ec.managementsystem.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TransferTypeFragment extends Fragment implements View.OnClickListener {

    ImageView ivPrepareOrder, ivReceiveOrder;

    public TransferTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_type, container, false);
        ivPrepareOrder = view.findViewById(R.id.iv_transfer_type_prepare);
        ivReceiveOrder = view.findViewById(R.id.iv_transfer_type_receive);
        ivPrepareOrder.setOnClickListener(this);
        ivReceiveOrder.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_transfer_type_prepare:
                NavHostFragment.findNavController(this).navigate(R.id.action_transferTypeFragment_to_preparePendingOrderFragment);
                break;
            case R.id.iv_transfer_type_receive:
                // Todo : go to receive orders activity
                break;
        }
    }
}