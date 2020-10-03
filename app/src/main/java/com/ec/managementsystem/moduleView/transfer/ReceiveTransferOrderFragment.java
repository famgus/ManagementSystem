package com.ec.managementsystem.moduleView.transfer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.DispatchedProduct;
import com.ec.managementsystem.clases.responses.GetProductsDispatchedResponse;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;
import com.ec.managementsystem.interfaces.OnItemClickListener;
import com.ec.managementsystem.moduleView.adapters.DispatchedProductsAdapter;
import com.ec.managementsystem.task.GetProductsDispatchedTaskController;
import com.ec.managementsystem.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReceiveTransferOrderFragment extends Fragment {

    private EditText etSeriesNumber, etOrderNumber;
    private Button btnSearch, btnRegister;
    private List<DispatchedProduct> dispatchedProducts = new ArrayList<>();
    private RecyclerView rvDispatchedProducts;
    private DispatchedProductsAdapter dispatchedProductsAdapter;
    private ReceivedProductViewModel receivedProductViewModel;
    private View view;
    private boolean startedToRegister = false;
    int totalRegistered = 0;

    public ReceiveTransferOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receivedProductViewModel = new ViewModelProvider(getActivity()).get(ReceivedProductViewModel.class);
        dispatchedProductsAdapter = new DispatchedProductsAdapter(dispatchedProducts, onItemClickListener());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_search_transfer_order, container, false);
            initializeViews(view);
            btnSearch.setOnClickListener(btnSearchListener());
            btnRegister.setOnClickListener(btnRegisterListener());
            btnRegister.setVisibility(View.GONE);
            initializeRV();
        }
        receivedProductViewModel.getDispatchedProductMutableLiveData().observe(getViewLifecycleOwner(), new Observer<DispatchedProduct>() {
            @Override
            public void onChanged(DispatchedProduct dispatchedProduct) {
                if(dispatchedProduct != null){
                    int index = dispatchedProducts.indexOf(dispatchedProduct);
                    dispatchedProductsAdapter.notifyItemChanged(index);
                    totalRegistered = totalRegistered+1;
                    boolean hasRegisterAll = totalRegistered == (dispatchedProducts.size()-1);
                    btnRegister.setVisibility(hasRegisterAll ? View.VISIBLE : View.GONE);
                }
            }
        });
        return view;
    }

    private void initializeViews(View view) {
        rvDispatchedProducts = view.findViewById(R.id.rv_searchtransferorder);
        etOrderNumber = view.findViewById(R.id.et_searchtransferorder_order_number);
        etSeriesNumber = view.findViewById(R.id.et_searchtransferorder_series);
        btnSearch = view.findViewById(R.id.btn_searchtransferorder_search);
        btnRegister = view.findViewById(R.id.btn_searchtransferorder_register);
    }

    private View.OnClickListener btnRegisterListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!startedToRegister){
                    dispatchedProductsAdapter.changeIsEditable();
                    btnRegister.setVisibility(View.GONE);
                    startedToRegister = true;
                }else{
                    requireActivity().finish();
                }
            }
        };
    }

    private void initializeRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDispatchedProducts.setLayoutManager(linearLayoutManager);
        rvDispatchedProducts.setAdapter(dispatchedProductsAdapter);
    }

    private OnItemClickListener<DispatchedProduct> onItemClickListener() {
        return new OnItemClickListener<DispatchedProduct>() {
            @Override
            public void onClick(DispatchedProduct item) {
                Bundle args = new Bundle();
                args.putSerializable("item", item);
                NavHostFragment.findNavController(ReceiveTransferOrderFragment.this).navigate(R.id.action_searchTransferOrderFragment_to_receiveTransferOrderDetailFragment, args);
            }
        };
    }

    private View.OnClickListener btnSearchListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etSeriesNumber.getText().toString().isEmpty() && !etOrderNumber.getText().toString().isEmpty()) {
                    String seriesNumber = etSeriesNumber.getText().toString().trim();
                    int orderNumber = Integer.parseInt(etOrderNumber.getText().toString());
                    GetProductsDispatchedTaskController dispatchTaskController = new GetProductsDispatchedTaskController(seriesNumber, orderNumber, getDispatchedProductResponseListener());
                    dispatchTaskController.execute();
                } else {
                    Toast.makeText(getContext(), "Debe llenar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private IDelegateResponseGeneric<GetProductsDispatchedResponse> getDispatchedProductResponseListener() {
        return new IDelegateResponseGeneric<GetProductsDispatchedResponse>() {
            @Override
            public void onResponse(GetProductsDispatchedResponse response) {
                if (response != null && response.getCode() == 200) {
                    if (response.getDispatchedProducts().size() > 0) {
                        btnSearch.setEnabled(false);
                        dispatchedProducts.clear();
                        dispatchedProducts.addAll(response.getDispatchedProducts());
                        dispatchedProducts.add(0, new DispatchedProduct(Utils.HEADER_TYPE));
                        dispatchedProductsAdapter.notifyDataSetChanged();
                        btnRegister.setEnabled(true);
                        btnRegister.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "No hay productos despachados", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error obteniendo la información solicitada", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}