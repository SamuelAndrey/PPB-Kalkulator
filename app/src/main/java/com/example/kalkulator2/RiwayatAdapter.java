package com.example.kalkulator2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {
    private ArrayList<Riwayat>listRiwayat;
    private Context context;
    private SharedPreferences sharedPreferences;

    public RiwayatAdapter(ArrayList<Riwayat> listRiwayat, Context context, SharedPreferences sharedPreferences) {
        this.listRiwayat = listRiwayat;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public RiwayatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        ViewHolder holder= new ViewHolder(inflater.inflate(R.layout.riwayat_layout, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.ViewHolder holder, int position) {
        Riwayat riwayat = listRiwayat.get(position);
        holder.riwayat.setText(riwayat.getRiwayat());
    }

    @Override
    public int getItemCount() { return listRiwayat.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView riwayat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            riwayat = itemView.findViewById(R.id.txtRwyt);

            // masih terdapat bug, jika baru membuka aplikasi dan terdapat histori yang tersimpan
            // jika mencoba mendelete data yang pertama terinput (paling bawah di recyclerview), aplikas akan force close
            // tapi jika masuk ke aplikasi data tetap terhapus

            itemView.setOnLongClickListener(view -> {

                int p = getLayoutPosition();
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Hapus Riwayat?")
                        .setMessage("Ingin Hapus Riwayat?")
                        .setPositiveButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                        .setNegativeButton("Yes", (dialogInterface, i) -> {
                            String id = listRiwayat.get(p).getId();

                            sharedPreferences.edit().remove(id).commit();

                            for (int j=0;j<listRiwayat.size();j++){
                                if (id.equalsIgnoreCase(listRiwayat.get(j).getId())){
                                    listRiwayat.remove(j);
                                    notifyItemChanged(j);
                                    notifyItemRangeChanged(j, listRiwayat.size());
                                }
                            }
                        });
                AlertDialog dialog = alert.create();
                dialog.show();

                return true;
            });
        }

    }
}
