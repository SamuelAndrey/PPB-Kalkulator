package com.example.kalkulator2;

import androidx.annotation.NonNull;

public class Riwayat {
    private String riwayat;
    private String id;

    public String getRiwayat() { return  riwayat; }

    public void setRiwayat(String riwayat) { this.riwayat = riwayat; }

    public String getId() { return  id; }

    public Riwayat(String id, String riwayat) {
        this.id = id;
        this.riwayat = riwayat;
    }

    @NonNull
    @Override
    public String toString() { return riwayat; }
}
