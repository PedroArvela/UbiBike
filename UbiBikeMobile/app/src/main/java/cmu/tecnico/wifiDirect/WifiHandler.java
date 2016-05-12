package cmu.tecnico.wifiDirect;

public interface WifiHandler {
    void wifiEnabled(boolean state);

    void peersChanged();

    void membChanged();

    void ownerChanged();
}
