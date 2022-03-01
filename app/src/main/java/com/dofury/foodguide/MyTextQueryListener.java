package com.dofury.foodguide;


import androidx.appcompat.widget.SearchView;

public class MyTextQueryListener implements SearchView.OnQueryTextListener {
    private TextSetAble iPostSearchProcedure;
    public MyTextQueryListener(TextSetAble text) {
        this.iPostSearchProcedure = text;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        iPostSearchProcedure.onSearchEnd(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    }





