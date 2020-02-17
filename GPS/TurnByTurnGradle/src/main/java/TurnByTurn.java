import com.esri.arcgisruntime.data.TransportationNetworkDataset;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.DrawStatus;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.*;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class TurnByTurn extends Application
{
    private MapView mapView;
    private RouteTask routeTask;
    private RouteParameters routeParameters;
    private TransportationNetworkDataset transportationNetwork;
    private ListView<String> directionsList = new ListView<>(  );
    List< Stop > routeStops = new ArrayList<>(  );

    private Graphic routeGraphic;
    private GraphicsOverlay routeGraphicsOverlay;

    private final SpatialReference ESPG_3857 = SpatialReference.create( 102100 );

    private static final int WHITE_COLOR = 0xffffffff;
    private static final int BLUE_COLOR = 0xFF0000FF;
    private static final int RED_COLOR = 0xFFFF0000;

    private Point startPoint = null;
    private Point endPoint = null;

    private StackPane stackPane;
    private VBox controlsVBox;
    private VBox searchBox;

    private TextField startPointBox;
    private TextField endPointBox;

    private GeocodeParameters geocodeParameters;
    private LocatorTask locatorTask;

    private String mmpkFile = "Greater_Los_Angeles.mmpk";
    private final MobileMapPackage mapPackage = new MobileMapPackage( mmpkFile );

    public static void main ( String[] args )
    {
        Application.launch( args );
    }

    @Override
    public void start ( Stage stage ) throws InterruptedException
    {
        System.out.println( "Start Function" );

        stackPane = new StackPane(  );
        Scene scene = new Scene( stackPane );

        stage.setTitle( "Navigation" );
        stage.setWidth( 900 );
        stage.setHeight( 800 );
        stage.show();
        stage.setScene( scene );


        controlsVBox = new VBox(6);
        controlsVBox.setBackground( new Background( new BackgroundFill( Paint.valueOf( "rgba(0,0,0,0.3)" ),
                CornerRadii.EMPTY, Insets.EMPTY ) ) );
        controlsVBox.setPadding( new Insets( 5.0 ) );
        controlsVBox.setMaxSize( 400,300 );
        controlsVBox.getStyleClass().add( "panel-region" );

        Label directionsLabel = new Label("Route directions");
        directionsLabel.getStyleClass().add( "panel-label" );

        controlsVBox.getChildren().addAll( directionsLabel, directionsList);
        mapView = new MapView();
        setupMobileMap();
        setupGraphicsOverlay();
        setupTextField();
        createLocatorTaskWithParameters();

        System.out.println( "exit start" );
    }

    private void setupGraphicsOverlay()
    {
        System.out.println( "SetupGraphicsOverlay Function" );

        if(mapView != null)
        {
            routeGraphicsOverlay = new GraphicsOverlay(  );
            mapView.getGraphicsOverlays().add( routeGraphicsOverlay );

        }
    }

    private void setupTextField()
    {
        searchBox = new VBox( 6 );
        searchBox.setPadding( new Insets( 5.0 ) );
        searchBox.setMaxSize( 400,100 );

        startPointBox = new TextField(  );
        startPointBox.setMaxWidth( 300 );
        startPointBox.setPromptText( "Enter starting address" );

        endPointBox = new TextField(  );
        endPointBox.setMaxWidth( 300 );
        endPointBox.setPromptText( "Enter destination address" );

        searchBox.getChildren().addAll( startPointBox, endPointBox );

        startPointBox.setOnAction( event -> {
            String query = startPointBox.getText();
            if(! "".equals( query ))
            {
                geocodeQuery(query, "start");
                //createRouteAndDisplay();
            }
        } );

        endPointBox.setOnAction( event -> {
            String query = endPointBox.getText();
            if(! "".equals( query ))
            {
                    geocodeQuery( query, "end" );
                    //createRouteAndDisplay();
            }
        } );
    }

    private void setupMobileMap() throws InterruptedException
    {
        System.out.println( "SetupMobileMap Function" );

        if(mapView != null)
        {
            Thread.sleep( 2000 );
            mapPackage.addDoneLoadingListener( () -> {

                if(mapPackage.getLoadStatus() == LoadStatus.LOADED && mapPackage.getMaps().size() > 0)
                {
                    double latitude = 34.05293;
                    double longitude = -118.24368;
                    double scale = 220000;

                    ArcGISMap map = mapPackage.getMaps().get(0);
                    transportationNetwork = map.getTransportationNetworks().get( 0 );
                    mapView.setMap( map );
                    map.setInitialViewpoint( new Viewpoint( latitude,longitude,scale ) );

                    stackPane.getChildren().addAll(mapView, controlsVBox, searchBox);
                    StackPane.setAlignment(controlsVBox, Pos.TOP_RIGHT);
                    StackPane.setMargin(controlsVBox, new Insets(10, 10, 0, 0));
                    StackPane.setAlignment( searchBox, Pos.TOP_LEFT );
                    StackPane.setMargin( searchBox, new Insets( 10, 5, 0, 0 ) );


                    System.out.println( "Calling setupRouteTask" );
                    setupRouteTask();

                } else if(mapPackage.getLoadStatus() == LoadStatus.LOADING)
                {
                    System.out.println( "Loading" );
                }
                else
                {
                    new Alert( Alert.AlertType.ERROR, "MMPK failed to load: "
                            + mapPackage.getLoadError().getMessage() ).show();
                }
            } );
            mapPackage.loadAsync();

        } else
        {
            System.out.println( "MapView was Null" );
        }
        System.out.println( "Exit SetupMobileMap Function" );
    }

    public void setupRouteTask()
    {
        System.out.println( "SetupRouteTask Function" );

        routeTask = new RouteTask( transportationNetwork );
        routeTask.loadAsync();

        routeTask.addDoneLoadingListener( () ->
        {
            if(routeTask.getLoadStatus() == LoadStatus.LOADED)
            {
                try
                {
                    routeParameters = routeTask.createDefaultParametersAsync().get();
                    routeParameters.setOutputSpatialReference( ESPG_3857 );

                    routeParameters.setReturnStops( true );
                    routeParameters.setReturnDirections( true );

                    //createRouteAndDisplay();
                    //solveForRoute();

                } catch (InterruptedException | ExecutionException e )
                {
                    new Alert( Alert.AlertType.ERROR, "Cannot create RouteTask parameters"
                            + e.getMessage()).show();
                }
            } else
            {
                new Alert( Alert.AlertType.ERROR, "Unable to load RouteTask" + routeTask.getLoadStatus().toString() ).show();
            }
        });
    }

    public void setMapMarker(Point location, SimpleMarkerSymbol.Style style, int markerColor, int outlineColor){
        final float markerSize = 8;
        final float markerOutlineThickness = 2;
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol( style, markerColor, markerSize );
        pointSymbol.setOutline(new SimpleLineSymbol( SimpleLineSymbol.Style.SOLID, outlineColor, markerOutlineThickness ) );
        Graphic pointGraphic = new Graphic( location, pointSymbol );
        routeGraphicsOverlay.getGraphics().add( pointGraphic );
    }

    public void setStartMarker(Point location)
    {
        System.out.println( "SetStartMarker Function" );

        routeGraphicsOverlay.getGraphics().clear();
        setMapMarker(location, SimpleMarkerSymbol.Style.DIAMOND,RED_COLOR,BLUE_COLOR );
        startPoint = location;
        endPoint = null;
    }

    public void setEndMarker( Point location )
    {
        System.out.println( "SetEndMarker Function" );

        setMapMarker(location, SimpleMarkerSymbol.Style.SQUARE,BLUE_COLOR,RED_COLOR);
        endPoint = location;
    }

    private void createLocatorTaskWithParameters()
    {
        locatorTask = mapPackage.getLocatorTask();
        geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add( "*" ); //return all parameters
        geocodeParameters.setMaxResults( 1 );   //get closest match
        geocodeParameters.setOutputSpatialReference( mapView.getSpatialReference() );
    }

    public void createRouteAndDisplay()
    {
        System.out.println( "CreateRouteAndDisplay Function" );

//        mapView.setOnMouseClicked( e -> {
//            if(e.getButton() == MouseButton.PRIMARY && e.isStillSincePress() )
//            {
                //Point2D point = new Point2D(e.getX(), e.getY());
                //Point mapPoint = mapView.screenToLocation( point );

                //List< Stop > routeStops = new ArrayList<>(  );
//                if(startPoint != null)
//                {
//                    //setStartMarker( mapPoint );
////                    TextSymbol startText = new TextSymbol( 10, "1", WHITE_COLOR,
////                            TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.MIDDLE );
////                    routeGraphicsOverlay.getGraphics().add( new Graphic( startPoint,startText ) );
//                    routeStops.add(new Stop(startPoint));
//                }else if (endPoint != null)
//                {
//                    //setEndMarker( mapPoint );
////                    TextSymbol endText = new TextSymbol( 10, "2", WHITE_COLOR,
////                            TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.MIDDLE );
////                    routeGraphicsOverlay.getGraphics().add( new Graphic( endPoint, endText ) );
//                    routeStops.add(new Stop(endPoint));
//                }else
//                {
//                    //setStartMarker( mapPoint );
////                    TextSymbol startText = new TextSymbol( 10, "1", WHITE_COLOR,
////                            TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.MIDDLE );
////                    routeGraphicsOverlay.getGraphics().add( new Graphic( startPoint,startText ) );
//                    System.out.println( "some weird stuff happened" );
//                }

//                routeParameters.setStops(routeStops);

//                if(startPoint != null && endPoint != null)
//                {
//                    solveForRoute();
//                }
//            }
//        } );
    }

    private void displayResult(GeocodeResult geocodeResult, String which)
    {
        String label = geocodeResult.getLabel();


        TextSymbol textSymbol = new TextSymbol( 18, label, 0xFF000000,
                TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM );
        Graphic textGraphic = new Graphic (geocodeResult.getDisplayLocation(), textSymbol);
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol( SimpleMarkerSymbol.Style.SQUARE, 0xFFFF0000, 12.0f );
        Graphic markerGraphic = new Graphic( geocodeResult.getDisplayLocation(), geocodeResult.getAttributes(), markerSymbol );
        routeGraphicsOverlay.getGraphics().addAll( Arrays.asList(markerGraphic,textGraphic));

        mapView.setViewpointCenterAsync( geocodeResult.getDisplayLocation() );

        if(which.equals( "start" ))
        {
            startPoint = geocodeResult.getDisplayLocation();
            //createRouteAndDisplay();
            routeStops.add(new Stop(startPoint));
        } else if (which.equals( "end" ))
        {
            endPoint = geocodeResult.getDisplayLocation();
            //createRouteAndDisplay();
            routeStops.add(new Stop(endPoint));
        } else
        {
            startPoint = geocodeResult.getDisplayLocation();
            //createRouteAndDisplay();
            routeStops.add(new Stop(startPoint));
        }
        routeParameters.setStops(routeStops);

        if(startPoint != null && endPoint != null)
        {
            solveForRoute();
        }


    }

    private void geocodeQuery(String query, String which)
    {
        ListenableFuture<List<GeocodeResult>> geocode = locatorTask.geocodeAsync( query, geocodeParameters );

        geocode.addDoneListener( () ->{
            try
            {
                List<GeocodeResult> results = geocode.get();

                if(results.size()> 0)
                {
                    GeocodeResult result = results.get( 0 );
                    displayResult(result, which);
                } else {
                    Alert alert = new Alert( Alert.AlertType.INFORMATION, "No results found" );
                    alert.show();
                }
            } catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
                Alert alert = new Alert (Alert.AlertType.ERROR, "Error getting result");
                alert.show();
            }
        } );
    }

    public void solveForRoute()
    {
        System.out.println( "SolveForRoute Function" );

        if(startPoint != null && endPoint != null)
        {
            try
            {
                RouteResult result = routeTask.solveRouteAsync( routeParameters ).get();
                List< Route > routes = result.getRoutes();
                if ( routes.size() < 1 )
                {
                    directionsList.getItems().add( "No Routes" );
                }
                Route route = routes.get( 0 );
                Geometry shape = route.getRouteGeometry();
                routeGraphic = new Graphic( shape, new SimpleLineSymbol( SimpleLineSymbol.Style.SOLID, BLUE_COLOR, 2 ) );
                routeGraphicsOverlay.getGraphics().add( routeGraphic );

                for ( DirectionManeuver step : route.getDirectionManeuvers() )
                {
                    directionsList.getItems().add( step.getDirectionText() );
                }

            }
            catch ( InterruptedException | ExecutionException e )
            {
                e.printStackTrace();
                new Alert( Alert.AlertType.ERROR, "Here" + e.getMessage() + e.getMessage() ).show();
            }
        }
    }

    @Override
    public void stop ()
    {
        if(mapView != null)
        {
            mapView.dispose();
        }
    }
}
