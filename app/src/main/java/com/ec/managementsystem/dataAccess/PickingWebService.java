package com.ec.managementsystem.dataAccess;

import android.util.Log;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.CustomerInvoiceForSendResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.TransfersOrderDetailForUserResponse;
import com.ec.managementsystem.util.Core;
import com.ec.managementsystem.util.MyApplication;
import com.ec.managementsystem.util.Utils;
import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.ec.managementsystem.dataAccess.WebServiceControl.GetURL;

public class PickingWebService {

    private static final String TAG = PickingWebService.class.getSimpleName();

    public static CustomerInvoiceForSendResponse getClientInvoicesForSend(int selectedOption){

        String message;
        CustomerInvoiceForSendResponse response = new CustomerInvoiceForSendResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetClientFacturasForSend";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("opcion", selectedOption);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);
                Log.d(TAG, "getClientInvoicesForSend: " + envelope.getResponse().toString());
                response = gson.fromJson(envelope.getResponse().toString(), CustomerInvoiceForSendResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetTrasladoDetail: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    public static GenericResponse registerIdPicking(String seriesNumber, int orderNumber, String idPicking){

        String message;
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "RegisterIdPicking";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numberSerie", seriesNumber);
                request.addProperty("numberPedido", orderNumber);
                request.addProperty("idpicking", idPicking);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);
                response = gson.fromJson(envelope.getResponse().toString(), GenericResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.RegisterIdPicking: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }
}
