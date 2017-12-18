package com.youngjoo.nsdchat;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

/**
 * Created by samsung on 2017. 12. 18..
 */

public class NSDHelper {

    public static final String SERVICE_TYPE = "_http._tcp";
    public static final String TAG = "NSDHelper";
    public String mServiceName = "NsdChat";

    Context mContext;
    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;

    NsdServiceInfo mNsdServiceInfo;

    public NSDHelper(Context context){
        mContext = context;
        mNsdManager = (NsdManager)context.getSystemService(Context.NSD_SERVICE);
    }

    public void initNSD(){
        initResolveListener();
        initDiscoveryListener();
        initRegisterationListener();
    }

    public void registerService(int port){
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceName(SERVICE_TYPE);

        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    private void initResolveListener(){
        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {
                Log.e(TAG, "Resolve failed - Error Code: "+i);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
                Log.i(TAG, "Resolve successful: "+nsdServiceInfo);
                if(nsdServiceInfo.getServiceName().equals(mServiceName)){
                    Log.i(TAG, "Same IP..");
                    return;
                }
                mNsdServiceInfo = nsdServiceInfo;
            }
        };
    }

    private void initDiscoveryListener(){
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String s, int i) {
                Log.e(TAG, "Discovery has failed - Error Code: "+i);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String s, int i) {
                Log.e(TAG, "Discovery has failed - Error Code: "+i);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onDiscoveryStarted(String s) {
                Log.i(TAG, "Network Service Discovery started...");
            }

            @Override
            public void onDiscoveryStopped(String s) {

            }

            @Override
            public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
                Log.i(TAG, "Network Service Discovery successful..");

                if(!nsdServiceInfo.getServiceType().equals(SERVICE_TYPE)){
                    Log.e(TAG, "Unknown service type: "+nsdServiceInfo.getServiceType());
                } else if(nsdServiceInfo.getServiceName().equals(mServiceName)){
                    Log.e(TAG, "Same machine: "+mServiceName);
                } else if(nsdServiceInfo.getServiceName().contains(mServiceName)){
                    mNsdManager.resolveService(nsdServiceInfo, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
                Log.e(TAG, "Service Lost..");
                if(mNsdServiceInfo.equals(nsdServiceInfo)){
                    mNsdServiceInfo = null;
                }
            }
        };
    }

    private void initRegisterationListener(){
        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {

            }

            @Override
            public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
                mServiceName = nsdServiceInfo.getServiceName();
            }
        };
    }


    public void discoverService(){
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public void stopDiscovery(){
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public NsdServiceInfo getChosenServiceInfo(){
        return mNsdServiceInfo;
    }

    public void tearDown(){
        mNsdManager.unregisterService(mRegistrationListener);
    }
}
