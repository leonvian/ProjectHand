package com.lvc.syndichand;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lvc.syndichand.database.CondominiumDAO;
import com.lvc.syndichand.database.UnityDAO;
import com.lvc.syndichand.model.Condominium;
import com.lvc.syndichand.model.Unity;
import com.lvc.syndichand.model.User;
import com.lvc.syndichand.webservice.WebFacade;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class OwnerList extends SyndicHandActivity implements OnDataSelected {

    private static final int CONDOMINIUN = 1;
    private static final int BLOCK = 2;
    private static final int COMMON_AREA = 3;
    private static final int CONSUME_ENTRY = 4;
    private static final int CONSUME_CONDOMINIUM = 5;
    private static final int VEHICLE_ENTRY = 6;

    private static final int REQUEST_CODE_ATUALIZE_LIST = 10;
    private static final int REQUEST_CODE_CONDOMINIUM_INFO = 11;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView listViewMenuDrawer;
    private MenuDrawerMaterialAdapter drawerAdapter;
    private DrawerLayout drawerLayout;

    private List<Unity> unities = null;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private List<MenuDrawerItem> drawerItens;
    private View layoutInstructionCreateCondominium;
    private View layoutListUnitys;
    private View viewInstructionsAlreadyEntried;
    private EditText editTextCondominiumCode;
    private View layoutInstructionNoUnities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_layout);
        prepareActionBar();

        layoutInstructionNoUnities = findViewById(R.id.layout_instruction_no_unities);

        editTextCondominiumCode = (EditText) findViewById(R.id.edit_text_condominium_code);

        viewInstructionsAlreadyEntried = findViewById(R.id.view_instructions_already_entried);

        layoutInstructionCreateCondominium = findViewById(R.id.layout_instruction_create_condominium);
        layoutListUnitys = findViewById(R.id.layout_list_unitys);


        findViewById(R.id.button_entry_condominium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextScreen(CondominiumEntry.class,REQUEST_CODE_CONDOMINIUM_INFO);
            }
        });

        findViewById(R.id.button_enter_condominium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"fxsJlXI6nj"
                showProgressDialog();
                String condominiumCode = editTextCondominiumCode.getText().toString();
                WebFacade.loadCondominiumByCondominiumCode(condominiumCode, new WebFacade.UniqueQueryWebCallback<Condominium>() {
                    @Override
                    public void onQueryResult(Condominium data, Exception e) {
                        if (e == null) {
                            data.save();
                            loadUnitiesIfHasCondominiumEntried();
                        } else {
                            dismissProgressDialog();
                            Toast.makeText(OwnerList.this, R.string.connection_problem_to_get_condominium_online, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        findViewById(R.id.button_already_entried).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = viewInstructionsAlreadyEntried.getVisibility();
                if (visibility == View.VISIBLE) {
                    viewInstructionsAlreadyEntried.setVisibility(View.INVISIBLE);
                } else {
                    viewInstructionsAlreadyEntried.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.fab_no_unities).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fabAction();
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              fabAction();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        unities = new ArrayList<Unity>();
        adapter = new OwnerListAdapter(this, this, unities);
        recyclerView.setAdapter(adapter);

        listViewMenuDrawer = (ListView) findViewById(R.id.menu_drawer_list);
        drawerItens = generateListMenuDrawerItems();
        drawerAdapter = new MenuDrawerMaterialAdapter(this, drawerItens);
        listViewMenuDrawer.setAdapter(drawerAdapter);

        LinearLayout leftDrawer = (LinearLayout) findViewById(R.id.drawer_prime);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        loadActionBarToggle();

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.onDrawerClosed(leftDrawer);

        listViewMenuDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuDrawerItem itemDrawer = (MenuDrawerItem) drawerAdapter.getItem(position);
                menuDrawerAction(itemDrawer);
            }
        });

        loadCondominiumByUserIfNecessary();

    }

    private void loadCondominiumByUser() {
        String condominiumCode = ParseUser.getCurrentUser().getString(User.CONDOMINIUM_ID_KEY);
        WebFacade.loadCondominiumByCondominiumCode(condominiumCode, new WebFacade.UniqueQueryWebCallback<Condominium>() {
            @Override
            public void onQueryResult(Condominium data, Exception e) {
                if(e == null) {
                    data.save();
                }

                loadUnitiesIfHasCondominiumEntried();
            }
        });
    }

    private void loadCondominiumByUserIfNecessary() {
        if(hasCondominium()) {
            loadUnitiesIfHasCondominiumEntried();
        } else {
            loadCondominiumByUser();
        }

    }

    private void fabAction() {
        Intent intent = new Intent(OwnerList.this, OwnerEntry.class);
        startActivityForResult(intent, REQUEST_CODE_ATUALIZE_LIST);
    }

    private void loadUnitiesIfHasCondominiumEntried() {
        if (hasCondominium()) {
            layoutInstructionCreateCondominium.setVisibility(View.GONE);
            layoutListUnitys.setVisibility(View.VISIBLE);
            loadListOfUnities();
        } else {
            layoutInstructionCreateCondominium.setVisibility(View.VISIBLE);
            layoutListUnitys.setVisibility(View.GONE);
        }
    }

    private boolean hasCondominium() {
        String condominiumID = CondominiumDAO.retrieveCondominiumIdentifier();
        if(condominiumID == null) {
            return false;
        } else {
            return true;
        }
    }

    private void loadListOfUnities() {
        showProgressDialog();
        WebFacade.retrieveListOfUnitys(new WebFacade.QueryWebCallback<Unity>() {
            @Override
            public void onQueryResult(List<Unity> data, Exception e) {
                dismissProgressDialog();

                if (e == null) {
                    unities = data;
                } else {
                    unities = UnityDAO.retrieveAll();
                }

                adapter = new OwnerListAdapter(OwnerList.this, OwnerList.this, unities);
                recyclerView.setAdapter(adapter);

                if(unities.isEmpty()) {
                    layoutInstructionNoUnities.setVisibility(View.VISIBLE);
                    layoutInstructionCreateCondominium.setVisibility(View.GONE);
                    layoutListUnitys.setVisibility(View.GONE);
                } else {
                    layoutInstructionNoUnities.setVisibility(View.GONE);
                    layoutListUnitys.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void prepareActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void menuDrawerAction(MenuDrawerItem itemDrawer) {

        int itemCode = itemDrawer.getCode();

        switch (itemCode) {

            case CONDOMINIUN:
                goToNextScreen(CondominiumEntry.class, REQUEST_CODE_CONDOMINIUM_INFO);
                break;

            case COMMON_AREA:
                goToNextScreen(CommonAreaList.class);
                break;

            case BLOCK:
                goToNextScreen(BlockList.class);
                break;

            case VEHICLE_ENTRY:
                goToNextScreen(VehicleList.class);
                break;
        }
    }

    private List<MenuDrawerItem> generateListMenuDrawerItems() {

        List<MenuDrawerItem> itens = new ArrayList<MenuDrawerItem>();

        itens.add(new MenuDrawerItem(CONDOMINIUN, getString(R.string.condominium)));
        itens.add(new MenuDrawerItem(BLOCK, getString(R.string.blocks)));
        itens.add(new MenuDrawerItem(COMMON_AREA, getString(R.string.common_areas)));
        itens.add(new MenuDrawerItem(CONSUME_ENTRY, getString(R.string.consume_entry)));
        itens.add(new MenuDrawerItem(CONSUME_CONDOMINIUM, getString(R.string.consume_condominium)));
        itens.add(new MenuDrawerItem(VEHICLE_ENTRY, getString(R.string.entry_vehicles)));

        return itens;
    }

    @Override
    public void onDataSelected(View view, int position) {
        Unity selectedOwner = unities.get(position);
        goToOwnerDetailScreen(selectedOwner);
    }

    private void goToOwnerDetailScreen(Unity owner) {
        Intent intent = new Intent(OwnerList.this, OwnerDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Unity.class.getSimpleName(), owner);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void loadActionBarToggle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        )

        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
                drawerAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_owners_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_style) {

            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                recyclerView.setLayoutManager(linearLayoutManager);
            } else {
                recyclerView.setLayoutManager(gridLayoutManager);
            }

        } else if (item.getItemId() == android.R.id.home) {
            actionBarDrawerToggle.onOptionsItemSelected(item);
        }
        return true;
    }

    private class MenuDrawerItem {

        private int code;
        private int drawerItemImage;
        private String drawerItemDescription;

        public MenuDrawerItem(int code, int drawerItemImage, String drawerItemDescription) {
            super();
            this.code = code;
            this.drawerItemImage = drawerItemImage;
            this.drawerItemDescription = drawerItemDescription;
        }

        public MenuDrawerItem(int code, String drawerItemDescription) {
            super();
            this.code = code;
            this.drawerItemDescription = drawerItemDescription;
        }

        public MenuDrawerItem(int code, int drawerItemImage, int drawerItemDescription) {
            this(code, drawerItemImage, getString(drawerItemDescription));
        }

        public MenuDrawerItem(int code, int drawerItemDescription) {
            this(code, getString(drawerItemDescription));
        }

        public int getDrawerItemImage() {
            return drawerItemImage;
        }

        public String getDrawerItemDescription() {
            return drawerItemDescription;
        }

        public int getCode() {
            return code;
        }
    }


    private class MenuDrawerMaterialAdapter extends ArrayAdapter<MenuDrawerItem> {

        private Activity context;

        public MenuDrawerMaterialAdapter(Activity context, List<MenuDrawerItem> groupItens) {
            super(context, 0, groupItens);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.menu_drawe_item, null);
            }

            MenuDrawerItem item = getItem(position);

            TextView textViewDrawerItem = (TextView) view.findViewById(R.id.text_view_item_drawer);
            textViewDrawerItem.setText(item.getDrawerItemDescription());

            ImageView imageViewItemDrawer = (ImageView) view.findViewById(R.id.image_view_item_drawer);
            imageViewItemDrawer.setImageResource(item.getDrawerItemImage());

            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CODE_ATUALIZE_LIST:
                    enableListLayoutIfNecessary();
                    Unity owner = (Unity) data.getExtras().getSerializable(Unity.class.getSimpleName());
                    if(unities.contains(owner)) {
                        int index = unities.indexOf(owner);
                        unities.set(index, owner);
                    } else {
                        recyclerView.scrollToPosition(0);
                        unities.add(0, owner);
                        adapter.notifyItemInserted(0);
                    }

                    adapter.notifyDataSetChanged();
                    showSnackBarHintToSync(owner);
                    break;

                case REQUEST_CODE_CONDOMINIUM_INFO:
                    loadUnitiesIfHasCondominiumEntried();
                    break;
            }
        }
    }

    private void enableListLayoutIfNecessary() {
        if(layoutListUnitys.getVisibility() != View.VISIBLE) {
            layoutInstructionNoUnities.setVisibility(View.GONE);
            layoutListUnitys.setVisibility(View.VISIBLE);
        }
    }


    private void showSnackBarHintToSync(final Unity owner) {
    /*    View parentLayout = findViewById(R.id.snackbarPosition);
        final Snackbar snackbar = Snackbar.make(parentLayout, R.string.owner_created_want_make_first_read, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOwnerDetailScreen(owner);
            }
        });

        snackbar.setDuration(Snackbar.LENGTH_LONG);
        snackbar.show(); */
    }


    private void showSnackBarNotPossibleToRetrieveCondominium() {
        View parentLayout = findViewById(R.id.snackbarPosition);
        final Snackbar snackbar = Snackbar.make(parentLayout, R.string.connection_problem_to_get_condominium_online, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        snackbar.setDuration(Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}

