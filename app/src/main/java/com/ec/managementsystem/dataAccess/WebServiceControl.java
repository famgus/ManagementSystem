package com.ec.managementsystem.dataAccess;

import android.util.Log;

import com.ec.managementsystem.BuildConfig;
import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.request.BarCodeRequest;
import com.ec.managementsystem.clases.request.BoxMasterRequest;
import com.ec.managementsystem.clases.request.PurchaseOrderRequest;
import com.ec.managementsystem.clases.request.PurchaseParentRequest;
import com.ec.managementsystem.clases.request.QualityControlRequest;
import com.ec.managementsystem.clases.request.ReturnProductChangeRequest;
import com.ec.managementsystem.clases.request.ReturnProductDetailsRequest;
import com.ec.managementsystem.clases.request.ReturnProductRequest;
import com.ec.managementsystem.clases.request.ReturnProductValidationRequest;
import com.ec.managementsystem.clases.responses.FacturasClientResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.ListFacturasDetasilResponse;
import com.ec.managementsystem.clases.responses.ListMotivesResponse;
import com.ec.managementsystem.clases.responses.ListPickingPedidoDetailResponse;
import com.ec.managementsystem.clases.responses.LoginResponse;
import com.ec.managementsystem.clases.responses.PedidoResponse;
import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;
import com.ec.managementsystem.clases.responses.ProductoResponse;
import com.ec.managementsystem.clases.responses.ReturnProductChangeResponse;
import com.ec.managementsystem.clases.responses.ReturnProductDetailsResponse;
import com.ec.managementsystem.clases.responses.ReturnProductListResponse;
import com.ec.managementsystem.clases.responses.ReturnProductValidationResponse;
import com.ec.managementsystem.util.Core;
import com.ec.managementsystem.util.MyApplication;
import com.ec.managementsystem.util.Utils;
import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.ec.managementsystem.moduleView.returnproduct.ReturnProductSelectChangeActivity.CODE_FLOW_MASTER_BOX;

public class WebServiceControl {

    private static String GetURL() {
        int port = 9298;
        if (BuildConfig.DEBUG) {
            String IP_DEBUG = "3.21.12.158";
            //IP_DEBUG = "10.238.26.69";
            port = 9298;
            return "http://" + IP_DEBUG + ":" + port + "/Service1.asmx";
        } else {
            String IP_RELEASE = "3.21.12.158";
            // IP_RELEASE = "10.238.26.69";
            return "http://" + IP_RELEASE + ":" + port + "/Service1.asmx";
        }
    }

    static public LoginResponse LogIn(String userName, String password, long date) {
        String message = "";
        LoginResponse response = new LoginResponse();
        try {
            if (Utils.IsOnline()) {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetVendedoresForLogin";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("userName", userName);
                request.addProperty("password", password);
                request.addProperty("date", date);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                Gson gson = new Gson();
                response = gson.fromJson(envelope.getResponse().toString(), LoginResponse.class);

                if (response != null) {
                    switch (response.getCode()) {
                        case 200:
                            message = MyApplication.GetAppContext().getString(R.string.ok);
                            break;
                        case 400:
                            message = MyApplication.GetAppContext().getString(R.string.text_error_authentication);
                            break;
                        default:
                            message = MyApplication.GetAppContext().getString(R.string.text_wrong_authentication);
                            break;
                    }
                }
                response.setMessage(message);
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.LogIn: " + e.getMessage());
            message = "Por favor intente de nuevo";
            response.setMessage(message);
            response.setCode(401);
        }
        return response;
    }

    static public PedidoResponse GetPedidoDetails(int number) {
        String message = "";
        PedidoResponse response = new PedidoResponse();
        try {
            if (Utils.IsOnline()) {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetPedidoDetails";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numero", number);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                Gson gson = new Gson();
                response = gson.fromJson(envelope.getResponse().toString(), PedidoResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetPedidoDetails: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public ProductoResponse GetProductDetails(String codigoBarra) {
        String message = "";
        ProductoResponse response = new ProductoResponse();
        try {
            if (Utils.IsOnline()) {
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetProductDetails";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("codigoBrras", codigoBarra);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                Gson gson = new Gson();
                response = gson.fromJson(envelope.getResponse().toString(), ProductoResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetProductDetails: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse InsertPurchaseOrder(PurchaseOrderRequest purchaseOrderRequest) {
        String message = "";
        GenericResponse response = new GenericResponse();
        PurchaseParentRequest purchaseParentRequest = new PurchaseParentRequest();
        purchaseParentRequest.setPedido(purchaseOrderRequest);
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "CreatePurchaseOrder";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("pedido", gson.toJson(purchaseParentRequest, PurchaseParentRequest.class));
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
            Utils.CreateLogFile("WebServiceControl.InsertPurchaseOrder: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse CreateBarCodeForBoxMaster(BarCodeRequest barCodeRequest) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "CreateBarCodeForBoxMaster";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("quantity", barCodeRequest.getQuantity());
                request.addProperty("userRegister", barCodeRequest.getUserRegister());
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
            Utils.CreateLogFile("WebServiceControl.CreateBarCodeForBoxMaster: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse CreateDespacho(BoxMasterRequest boxMasterRequest) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "CreateDespacho";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("barCodeArticle", boxMasterRequest.getBarCodeArticle());
                request.addProperty("quantityArticle", boxMasterRequest.getQuantityArticle());
                request.addProperty("barCodeBoxMasterOrigin", boxMasterRequest.getBarCodeBoxMasterOrigin());
                request.addProperty("codeStorage", boxMasterRequest.getCodeStorage());
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
            Utils.CreateLogFile("WebServiceControl.CreateDespacho: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse CreateIngreso(BoxMasterRequest boxMasterRequest) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "CreateIngreso";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("barCodeArticle", boxMasterRequest.getBarCodeArticle());
                request.addProperty("quantityArticle", boxMasterRequest.getQuantityArticle());
                request.addProperty("barCodeBoxMasterOrigin", boxMasterRequest.getBarCodeBoxMasterOrigin());
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
            Utils.CreateLogFile("WebServiceControl.CreateIngreso: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse CreateReubicacionArticle(BoxMasterRequest boxMasterRequest) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "CreateReubicacionArticle";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("barCodeArticle", boxMasterRequest.getBarCodeArticle());
                request.addProperty("quantityArticle", boxMasterRequest.getQuantityArticle());
                request.addProperty("barCodeBoxMasterOrigin", boxMasterRequest.getBarCodeBoxMasterOrigin());
                request.addProperty("barCodeBoxMasterDestiny", boxMasterRequest.getBarCodeBoxMasterDestiny());
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
            Utils.CreateLogFile("WebServiceControl.CreateTraslado: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse CreateReubicacionBoxMaster(BoxMasterRequest boxMasterRequest) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "CreateReubicacionBoxMaster";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("barCodeBoxMasterOrigin", boxMasterRequest.getBarCodeBoxMasterOrigin());
                request.addProperty("ubicacionOrigin", boxMasterRequest.getUbicacionOrigen());
                request.addProperty("ubicacionDestiny", boxMasterRequest.getUbicacionDestiny());
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
            Utils.CreateLogFile("WebServiceControl.CreateTraslado: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }


    static public FacturasClientResponse GetClientFacturasPacking(String numberClient) {
        String message = "";
        FacturasClientResponse response = new FacturasClientResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetClientFacturasPacking";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numberClient", numberClient);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response = gson.fromJson(envelope.getResponse().toString(), FacturasClientResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetClientFacturasPacking: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public ListFacturasDetasilResponse GetFacturaDetail(String numberSerie, Integer numberAlbaran) {
        String message = "";
        ListFacturasDetasilResponse response = new ListFacturasDetasilResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetFacturaDetail";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numberSerie", numberSerie);
                request.addProperty("numberAlbaran", numberAlbaran);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response = gson.fromJson(envelope.getResponse().toString(), ListFacturasDetasilResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetFacturaDetail: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public ListPickingPedidoDetailResponse GetPickingPedidoDetail(String numberSerie, Integer numberPedido) {
        String message = "";
        ListPickingPedidoDetailResponse response = new ListPickingPedidoDetailResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetPickingPedidoDetail";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numberSerie", numberSerie);
                request.addProperty("numberPedido", numberPedido);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response = gson.fromJson(envelope.getResponse().toString(), ListPickingPedidoDetailResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetPickingPedidoDetail: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse UpdatePicking(String numberSerie, Integer numberPedido, Integer codeArticle, Integer quantity, String barCodeMaster, String barCodeLocation) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "UpdatePicking";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numberSerie", numberSerie);
                request.addProperty("numberPedido", numberPedido);
                request.addProperty("codeArticle", codeArticle);
                request.addProperty("quantity", quantity);
                request.addProperty("barCodeMaster", barCodeMaster);
                request.addProperty("barCodeLocation", barCodeLocation);
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
            Utils.CreateLogFile("WebServiceControl.UpdatePicking: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse UpdatePickingPedidoState(String numberSerie, Integer numberPedido, Integer state) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "UpdatePickingPedidoState";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numberSerie", numberSerie);
                request.addProperty("numberPedido", numberPedido);
                request.addProperty("state", state);
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
            Utils.CreateLogFile("WebServiceControl.UpdatePickingPedidoState: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public PickingPedidoUserResponse GetPedidoPickingForUser() {
        String message = "";
        PickingPedidoUserResponse response = new PickingPedidoUserResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetPedidoPickingForUser";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response = gson.fromJson(envelope.getResponse().toString(), PickingPedidoUserResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetPedidoPickingForUser: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public FacturasClientResponse SearchFacturasSend(int numberPedido, String numberSerie) {
        String message = "";
        FacturasClientResponse response = new FacturasClientResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "SearchFacturasSend";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numberPedido", numberPedido);
                request.addProperty("numberSerie", numberSerie);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response = gson.fromJson(envelope.getResponse().toString(), FacturasClientResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetClientFacturasPacking: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse ValidateBoxMasterCodeBar(String barCode, Integer state) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "ValidateBoxMasterCodeBar";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("barCode", barCode);
                request.addProperty("state", state);
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
            Utils.CreateLogFile("WebServiceControl.ValidateBoxMasterCodeBar: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse ValidateExistBoxMaster(String barCode) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "ValidateExistBoxMaster";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("codigoBrras", barCode);
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
            Utils.CreateLogFile("WebServiceControl.ValidateExistBoxMaster: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }


    static public ListMotivesResponse GetMotivesQualityControl() {
        String message = "";
        ListMotivesResponse response = new ListMotivesResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetMotivesQualityControl";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response = gson.fromJson(envelope.getResponse().toString(), ListMotivesResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetMotivesQualityControl: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse InsertQualityControl(QualityControlRequest qualityControlRequest) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "InsertQualityControl";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("barCode", qualityControlRequest.getBarCode());
                request.addProperty("codeLocation", qualityControlRequest.getCodeLocation());
                request.addProperty("motive", qualityControlRequest.getMotive());
                request.addProperty("descriptionMotive", qualityControlRequest.getDescriptionMotive());
                request.addProperty("user", qualityControlRequest.getUser());
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
            Utils.CreateLogFile("WebServiceControl.InsertQualityControl: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public GenericResponse ValidateLocationCodeBar(String barCode) {
        String message = "";
        GenericResponse response = new GenericResponse();
        try {
            if (Utils.IsOnline()) {
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "ValidateLocationCodeBar";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("barCode", barCode);
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
            Utils.CreateLogFile("WebServiceControl.ValidateLocationCodeBar: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }


    static public ReturnProductListResponse findReturnProductOrder(ReturnProductRequest returnProductRequest) {
        String message = "";
        ReturnProductListResponse response = new ReturnProductListResponse();
        try {
            if (Utils.IsOnline()) {
                Log.i("findReturnProductOrder", String.valueOf(returnProductRequest.getOrder()));
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetReturnOrdenByNumber";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("numero", returnProductRequest.getOrder());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response = gson.fromJson(envelope.getResponse().toString(), ReturnProductListResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetReturnOrderDetail: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public ReturnProductChangeResponse saveReturnProduct(ReturnProductChangeRequest returnProductChangeRequest) {
        String message = "";
        ReturnProductChangeResponse response = new ReturnProductChangeResponse();
        try {
            if (Utils.IsOnline()) {
                Log.i("ReturnProductListRes", returnProductChangeRequest.getDetail());
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "SaveReturnDetail";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("detail", returnProductChangeRequest.getDetail());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response = gson.fromJson(envelope.getResponse().toString(), ReturnProductChangeResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetReturnOrderDetail: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public ReturnProductValidationResponse validationMasterBoxUbication(ReturnProductValidationRequest returnProductValidationRequest) {
        String message = "";
        ReturnProductValidationResponse response = new ReturnProductValidationResponse();
        try {
            if (Utils.IsOnline()) {
                Log.i("validationMasterBoxUbic", returnProductValidationRequest.getData());
                Log.i("validationMasterBoxUbic", String.valueOf(returnProductValidationRequest.getTypeValidation()));
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = (returnProductValidationRequest.getTypeValidation() == CODE_FLOW_MASTER_BOX)
                        ? "ValidateExistBoxMaster" : "ValidateLocationCodeBar";
                Log.i("validationMasterBoxUbic", METHOD_NAME);
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                if (returnProductValidationRequest.getTypeValidation() == CODE_FLOW_MASTER_BOX) {
                    request.addProperty("codigoBarras", returnProductValidationRequest.getData());
                } else {
                    request.addProperty("barCode", returnProductValidationRequest.getData());
                }

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response.setTypeValidation(returnProductValidationRequest.getTypeValidation());
                response = gson.fromJson(envelope.getResponse().toString(), ReturnProductValidationResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetReturnOrderDetail: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }

    static public ReturnProductDetailsResponse findProductById(ReturnProductDetailsRequest returnProductDetailsRequest) {
        String message = "";
        ReturnProductDetailsResponse response = new ReturnProductDetailsResponse();
        try {
            if (Utils.IsOnline()) {
                Log.i("GetProductDetailReturn", String.valueOf(returnProductDetailsRequest.getBarCode()));
                Gson gson = new Gson();
                final String NAMESPACE = "http://tempuri.org/";
                final String METHOD_NAME = "GetProductDetailReturn";
                final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("codigoBarras", returnProductDetailsRequest.getBarCode());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.implicitTypes = true;
                envelope.setAddAdornments(false);
                envelope.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(GetURL(), Core.TIME_OUT_WEB_SERVICES);
                htse.debug = true;
                htse.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
                htse.call(SOAP_ACTION, envelope);

                response = gson.fromJson(envelope.getResponse().toString(), ReturnProductDetailsResponse.class);
                return response;
            } else {
                message = MyApplication.GetAppContext().getString(R.string.no_internet);
                response.setMessage(message);
            }
        } catch (Exception e) {
            Utils.CreateLogFile("WebServiceControl.GetReturnOrderDetail: " + e.getMessage());
            message = e.getMessage();
            response.setCode(401);
            response.setMessage(message);
        }
        return response;
    }
}
