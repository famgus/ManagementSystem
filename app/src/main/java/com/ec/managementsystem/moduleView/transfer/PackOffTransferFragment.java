package com.ec.managementsystem.moduleView.transfer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.TransferPackOff;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.GetDispatchResponse;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;
import com.ec.managementsystem.interfaces.OnItemClickListener;
import com.ec.managementsystem.moduleView.adapters.PackOffTransferAdapter;
import com.ec.managementsystem.task.DispatchTaskController;
import com.ec.managementsystem.task.UpdateStateBoxDispatchTaskController;
import com.ec.managementsystem.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class PackOffTransferFragment extends Fragment implements OnItemClickListener<TransferPackOff> {

    private Button btnSearch, btnRegister;
    private EditText etSeriesNumber, etOrderNumber, etTransport;
    private RecyclerView rvPackOffTransfer;
    private List<TransferPackOff> transferPackOffList = new ArrayList<>();
    private PackOffTransferAdapter packOffTransferAdapter;

    public PackOffTransferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pack_off_transfer, container, false);
        btnSearch = view.findViewById(R.id.btn_packoff_search);
        btnRegister = view.findViewById(R.id.btn_packoff_register);
        etOrderNumber = view.findViewById(R.id.et_packofftransfer_order_number);
        etSeriesNumber = view.findViewById(R.id.et_packofftransfer_series);
        rvPackOffTransfer = view.findViewById(R.id.rv_packofftransfer);
        etTransport = view.findViewById(R.id.et_packoff_transport);
        btnSearch.setOnClickListener(searchButtonListener());
        btnRegister.setOnClickListener(registerListener());
        initializeRV();
        return view;
    }

    private void initializeRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvPackOffTransfer.setLayoutManager(linearLayoutManager);
        packOffTransferAdapter = new PackOffTransferAdapter(transferPackOffList, this);
        rvPackOffTransfer.setAdapter(packOffTransferAdapter);
    }

    private View.OnClickListener registerListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTransport.getText().toString().isEmpty()){
                    String transportId = etTransport.getText().toString().trim();
                    String barCodes = getCheckedBarCodes();
                    UpdateStateBoxDispatchTaskController updateStateBoxDispatchTaskController = new UpdateStateBoxDispatchTaskController(barCodes, transportId, registerResponse());
                    updateStateBoxDispatchTaskController.execute();
                }else{
                    Toast.makeText(getContext(), "Debe ingresar el Id del transporte", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private String getCheckedBarCodes() {
        List<String> barCodes = new ArrayList<>();
        for(TransferPackOff transferPackOff : transferPackOffList){
            if(transferPackOff.isChecked()){
                barCodes.add(transferPackOff.getBoxMasterBarCode());
            }
        }
        return TextUtils.join(",", barCodes);
    }

    private View.OnClickListener searchButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etSeriesNumber.getText().toString().isEmpty() && !etOrderNumber.getText().toString().isEmpty()) {
                    String seriesNumber = etSeriesNumber.getText().toString().trim();
                    int orderNumber = Integer.parseInt(etOrderNumber.getText().toString());
                    DispatchTaskController dispatchTaskController = new DispatchTaskController(seriesNumber, orderNumber, dispatchResponseListener());
                    dispatchTaskController.execute();
                } else {
                    Toast.makeText(getContext(), "Debe llenar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private IDelegateResponseGeneric<GetDispatchResponse> dispatchResponseListener() {
        return new IDelegateResponseGeneric<GetDispatchResponse>() {
            @Override
            public void onResponse(GetDispatchResponse response) {
                if (response != null && response.getCode() == 200) {
                    if (response.getTransferPackOffs().size() > 0) {
                        transferPackOffList.clear();
                        transferPackOffList.addAll(response.getTransferPackOffs());
                        transferPackOffList.add(0, new TransferPackOff(Utils.HEADER_TYPE));
                        packOffTransferAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "No hay productos por despachar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error obteniendo los datos", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private boolean showRegisterButton() {
        for (TransferPackOff packOff : transferPackOffList) {
            if (packOff.isChecked()) {
                return true;
            }
        }
        return false;
    }

    private IDelegateResponseGeneric<GenericResponse> registerResponse(){
        return new IDelegateResponseGeneric<GenericResponse>() {
            @Override
            public void onResponse(GenericResponse response) {
                if(response != null && response.getCode() == 200){
                    Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(PackOffTransferFragment.this).navigateUp();
                }else{
                    Toast.makeText(getContext(), "Error registrando la información", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onClick(TransferPackOff item) {
        btnRegister.setVisibility(showRegisterButton() ? View.VISIBLE : View.GONE);
    }
}