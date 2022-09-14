package com.dofury.foodguide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dofury.foodguide.inform.FoodInform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends Fragment{
    private View view;
    public ArrayList<Food> foodList = new ArrayList<Food>();
    ListView listView;
    SearchView searchview;
    String text;
    String preFrag;
    private Activity activity;
    private String selFoodName = "";
    private int flag;
    public static FoodList newInstance(){
        return new FoodList();
    }
    public static FoodList newInstance(String text,String preFrag){
        return new FoodList(text,preFrag);
    }
    public static FoodList newInstance(String preFrag){
        return new FoodList(preFrag);
    }
    public FoodList(String text,String preFrag)
    {
        this.text =text;
        this.preFrag = preFrag;
    }
    public FoodList(String preFrag)
    {
        this.preFrag = preFrag;
    }
    public FoodList()
    {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.food_list, container, false);
        activity = (Activity) getActivity();
        searchview = view.findViewById(R.id.food_search_view);
        textListener();
        switch(preFrag) {
            case "main":
                setUpData();
                flag = 1;
                searchview.setQuery(text, true);
                break;
            case "table":
                setUpData();
                flag = 2;
                break;
            default:
                setUpData();
                break;
        }
        return view;
    }

    public void textListener(){
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                selFoodName = query;
                searchFood();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                 /* ArrayList<Food> filterFood = new ArrayList<>();
                for(int i =0;i<foodList.size();i++){
                    Food food = foodList.get(i);
                    //데이터와 비교해서 내가 쓴 동물 이름이 있다면
                    if(food.getName().toLowerCase().contains(newText.toLowerCase())){
                        filterFood.add(food);
                    }
                }
                FoodAdapter adapter = new FoodAdapter(view.getContext(),0,filterFood);
                listView.setAdapter(adapter);*/
                return false;
            }
        });

    }

    public void searchFood(){
        if(flag == 1 && foodList.isEmpty()) return;
        ArrayList<Food> filterFood = new ArrayList<>();
        for(int i =0;i<foodList.size();i++){
            Food food = foodList.get(i);

            //데이터와 비교해서 내가 쓴 동물 이름이 있다면
            if(food.getName().toLowerCase().contains(selFoodName.toLowerCase())){
                filterFood.add(food);
            }
        }
        FoodAdapter adapter = new FoodAdapter(view.getContext(),0,filterFood);
        listView.setAdapter(adapter);

    }

    //데이터셋팅//
    private void setUpData() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("FoodGuide");
        dbRef.child("Food").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    Food food = new Food();

                    FoodInform foodInform =  dataSnapshot.child("foodInform").getValue(FoodInform.class);
                    food.setFoodInform(foodInform);
                    food.setId(dataSnapshot.child("id").getValue().toString());
                    food.setName(dataSnapshot.child("name").getValue().toString());
                    food.setImage(dataSnapshot.child("image").getValue().toString());
                    food.setComment(dataSnapshot.child("comment").getValue().toString());
                    String json = dataSnapshot.child("like").getValue().toString();

                    if(json.isEmpty()) {
                        food.setLike("[]");
                    } else {
                        food.setLike(json);
                    }

                    foodList.add(food);

                }
                setUpList();
            }
        });

    }
    //리스트 셋팅//
    private void setUpList(){
        listView = view.findViewById(R.id.food_listView);
        FoodAdapter adapter = new FoodAdapter(view.getContext(), R.layout.food_item, foodList);
        listView.setAdapter(adapter);

        if(preFrag.equals("main")) {
            setUpOnClickListener();
        } else if(preFrag.equals("table")) {
            setFoodOnClickListener();
        } else {
            setUpOnClickListener();
        }

        searchFood();
    }

    //상세 페이지 이벤트//
    private void setUpOnClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Food selectFood = (Food) listView.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                DetailFood detailFood = new DetailFood();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                bundle.putParcelable("ClickedFood", selectFood);
                detailFood.setArguments(bundle);
                transaction.replace(R.id.main_frame, detailFood);
                transaction.commit();

            }
        });
    }

    private void setFoodOnClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(activity.mFragment != null)
                {
                    Food selectFood = (Food) listView.getItemAtPosition(position);
                    Bundle bundle = new Bundle();
                    Table table = new Table();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    bundle.putParcelable("foodlist", selectFood);
                    bundle.putString("preFrag", "foodlist");
                    bundle.putString("foodName", selectFood.getName());
                    table.setArguments(bundle);
                    transaction.replace(R.id.main_frame, table);
                    transaction.commit();
                }
            }
        });
    }

}

