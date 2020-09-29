package com.ec.managementsystem.dataAccess;

import android.util.Log;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.request.UpdateProductReceivedRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.GetDispatchResponse;
import com.ec.managementsystem.clases.responses.GetProductsDispatchedResponse;
import com.ec.managementsystem.util.Core;
import com.ec.managementsystem.util.MyApplication;
import com.ec.managementsystem.util.Utils;
import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.ec.managementsystem.dataAccess.WebServiceControl.GetURL;

public class TransferWebServiceControl {

    private static final String TAG = TransferWebServiceControl.class.getSimpleName();

    public static GetDispatchResponse getDispatch(String seriesNumber, int orderNumber){
        String message;
        GetDispatchResponse response = new GetDispatchResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetDispatch";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numeroSerie", seriesNumber);
                request.addProperty("numeroPedido", orderNumber);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);
                response = gson.fromJson(envelope.getResponse().toString(), GetDispatchResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetDispatch: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    public static GetProductsDispatchedResponse getProductsDispatched(String seriesNumber, int orderNumber){
        String message;
        GetProductsDispatchedResponse response = new GetProductsDispatchedResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetProductsDispatched";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numserie", seriesNumber);
                request.addProperty("numpedido", orderNumber);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);
                response = gson.fromJson(envelope.getResponse().toString(), GetProductsDispatchedResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetProductsDispatched: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    public static GenericResponse updateProductReceived(UpdateProductReceivedRequest params){
        String message;
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "UpdateProductReceived";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numserie", params.getSeriesNumber());
                request.addProperty("numpedido", params.getOrderNumber());
                request.addProperty("codArticulo", params.getProductCode());
                request.addProperty("talla", params.getSize());
                request.addProperty("color", params.getColor());
                request.addProperty("cantidadrecibida", params.getReceivedQuantity());
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
            Utils.CreateLogFile("WebServiceControl.UpdateProductReceived: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    public static GenericResponse updateStateBoxDispatch(String barCodes, String transportId){
        String message;
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "UpdateStateBoxDispatch";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("boxMasterCodesBar", barCodes);
                request.addProperty("trasporte", transportId);
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
            Utils.CreateLogFile("WebServiceControl.UpdateStateBoxDispatch: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    public static GenericResponse createBoxForDsispatch(String transfers){
        String message;
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "CreateBoxForDispatch";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("traspaso", transfers);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);
                Log.d(TAG, "createBoxForDsispatch: " + envelope.getResponse().toString());
                response = gson.fromJson(envelope.getResponse().toString(), GenericResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.CreateBoxForDsispatch: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }
}
