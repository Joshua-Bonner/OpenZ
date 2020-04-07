import com.esri.arcgisruntime.data.TransportationNetworkDataset;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.*;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static java.lang.System.exit;

public class TurnByTurn extends Application
{
    private MapView mapView;
    private RouteTask routeTask;
    private RouteParameters routeParameters;
    private TransportationNetworkDataset transportationNetwork;
    List< Stop > routeStops = new ArrayList<>();
    private ListView< String > NextTurn = new ListView<>();

    private Graphic routeGraphic;
    private GraphicsOverlay routeGraphicsOverlay;

    private final SpatialReference _4326 = SpatialReference.create( 4326 );

    private static final int BLUE_COLOR = 0xFF0000FF;
    private static final int RED_COLOR = 0xFFFF0000;

    private Point startPoint = null;
    private Point endPoint = null;
    private Point currentPoint = null;

    private StackPane stackPane;
    private VBox searchBox;
    private VBox turnByTurnBox;

    private TextField endPointBox;

    private GeocodeParameters geocodeParameters;
    private LocatorTask locatorTask;

    private String mmpkFile = "Greater_Los_Angeles.mmpk";
    private final MobileMapPackage mapPackage = new MobileMapPackage( mmpkFile );

    int GPSCoordCount = 0;
    boolean atEnd;

    // THE FOLLOWING IS USED TO ACCESS DATA FROM THE ANTENNA
    // UNBLOCK FOLLOWING BLOCK ONCE THE ANTENNA HAS BEEN CONNECTED
    /*
    SerialPortInfo myPortInfo = new SerialPortInfo(
    	"/dev/ttyUSB0", BaudRate.BAUD_4800, Parity.NONE, StopBits.ONE, 7);
		SerialPortGPSWatcher myWatcher = new SerialPortGPSWatcher(myPortInfo);
		GPSLayer gpsLayer = new GPSLayer(myWatcher);
		gpsLayer.setMode(Mode.NAVIGATION);
		gpsLayer.setNavigationPointHeightFactor(0.3);
		*/

    public static void main ( String[] args )
    {
        Application.launch( args );
    }

    @Override
    public void start ( Stage stage ) throws InterruptedException
    {
        System.out.println( "Start Function" );
        stackPane = new StackPane();
        Scene scene = new Scene( stackPane );

        stage.setTitle( "Navigation" );
        stage.setWidth( 800 );
        stage.setHeight( 480 );
        stage.show();
        stage.setScene( scene );

        turnByTurnBox = new VBox( 6 );
        turnByTurnBox.setBackground( new Background( new BackgroundFill( Paint.valueOf( "rgba(0,0,0,0.3)" ),
                CornerRadii.EMPTY, Insets.EMPTY ) ) );
        turnByTurnBox.setPadding( new Insets( 5.0 ) );
        turnByTurnBox.setMaxSize( 300, 200 );
        turnByTurnBox.getStyleClass().add( "panel-region" );
        Label turnLabel = new Label( "Next Turn" );
        turnLabel.getStyleClass().add( "panel-region" );

        turnByTurnBox.getChildren().addAll( turnLabel, NextTurn );

        mapView = new MapView();
        setupMobileMap();
        setupGraphicsOverlay();
        setupTextField();
        createLocatorTaskWithParameters();

        System.out.println( "exit start" );
    }

    private void setupGraphicsOverlay ()
    {
        System.out.println( "SetupGraphicsOverlay Function" );

        if ( mapView != null )
        {
            routeGraphicsOverlay = new GraphicsOverlay();
            mapView.getGraphicsOverlays().add( routeGraphicsOverlay );
        }
        System.out.println( "Exit SetupGraphicsOverlay Function" );
    }

    private void setupTextField ()
    {
        searchBox = new VBox( 6 );
        searchBox.setPadding( new Insets( 5.0 ) );
        searchBox.setMaxSize( 400, 100 );

        endPointBox = new TextField();
        endPointBox.setMaxWidth( 300 );
        endPointBox.setPromptText( "Enter destination address" );

        searchBox.getChildren().addAll( endPointBox );

        getGPSStartPoint();

        endPointBox.setOnAction( event ->
        {
            String query = endPointBox.getText();
            if ( !"".equals( query ) )
            {
                geocodeQuery( query, "end" );
            }
        } );
        System.out.println( "exit setupT ext Field" );
    }

    public void getGPSStartPoint ()
    {
        MockCoordinates mc = new MockCoordinates();
        startPoint = new Point( mc.x, mc.y, _4326 );
    }

    public void getGPSCurrentPoint ()
    {
        MockCoordinates mc = new MockCoordinates();
        currentPoint = new Point( mc.getCurrentXCoord( GPSCoordCount ), mc.getCurrentYCoord( GPSCoordCount ), _4326 );
        atEnd = mc.atEndpoint( GPSCoordCount );
        GPSCoordCount++;
        System.out.println( currentPoint );
    }

    private void setupMobileMap ()
    {
        System.out.println( "SetupMobileMap Function" );

        if ( mapView != null )
        {
            mapPackage.addDoneLoadingListener( () ->
            {

                if ( mapPackage.getLoadStatus() == LoadStatus.LOADED && mapPackage.getMaps().size() > 0 )
                {
                    double latitude = 34.05293;
                    double longitude = -118.24368;
                    double scale = 220000;

                    ArcGISMap map = mapPackage.getMaps().get( 0 );
                    transportationNetwork = map.getTransportationNetworks().get( 0 );
                    mapView.setMap( map );
                    map.setInitialViewpoint( new Viewpoint( latitude, longitude, scale ) );

                    stackPane.getChildren().addAll( mapView, searchBox, turnByTurnBox );
                    StackPane.setAlignment( searchBox, Pos.TOP_LEFT );
                    StackPane.setMargin( searchBox, new Insets( 10, 5, 0, 0 ) );
                    StackPane.setAlignment( turnByTurnBox, Pos.BOTTOM_CENTER );
                    StackPane.setMargin( turnByTurnBox, new Insets( 0, 0, 10, 0 ) );

                    System.out.println( "Calling setupRouteTask" );
                    setupRouteTask();

                }
                else if ( mapPackage.getLoadStatus() == LoadStatus.LOADING )
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

        }
        else
        {
            System.out.println( "MapView was Null" );
        }
    }

    public void setupRouteTask ()
    {
        System.out.println( "SetupRouteTask Function" );

        routeTask = new RouteTask( transportationNetwork );
        routeTask.loadAsync();

        routeTask.addDoneLoadingListener( () ->
        {
            if ( routeTask.getLoadStatus() == LoadStatus.LOADED )
            {
                try
                {
                    routeParameters = routeTask.createDefaultParametersAsync().get();
                    routeParameters.setOutputSpatialReference( mapView.getSpatialReference() );

                    routeParameters.setReturnStops( true );
                    routeParameters.setReturnDirections( true );
                }
                catch ( InterruptedException | ExecutionException e )
                {
                    new Alert( Alert.AlertType.ERROR, "Cannot create RouteTask parameters"
                            + e.getMessage() ).show();
                }
            }
            else
            {
                new Alert( Alert.AlertType.ERROR, "Unable to load RouteTask" + routeTask.getLoadStatus().toString() ).show();
            }
        } );
        System.out.println( "Exit setup route task" );
    }

    public void setMapMarker ( Point location, SimpleMarkerSymbol.Style style, int markerColor, int outlineColor )
    {
        final float markerSize = 8;
        final float markerOutlineThickness = 2;
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol( style, markerColor, markerSize );
        pointSymbol.setOutline( new SimpleLineSymbol( SimpleLineSymbol.Style.SOLID, outlineColor, markerOutlineThickness ) );
        Graphic pointGraphic = new Graphic( location, pointSymbol );
        routeGraphicsOverlay.getGraphics().add( pointGraphic );
    }

    public void setStartMarker ( Point location )
    {
        setMapMarker( location, SimpleMarkerSymbol.Style.DIAMOND, RED_COLOR, BLUE_COLOR );
        mapView.setViewpointCenterAsync( startPoint, 11000 );
    }

    public void setEndMarker ( Point location )
    {
        setMapMarker( location, SimpleMarkerSymbol.Style.SQUARE, BLUE_COLOR, RED_COLOR );
        endPoint = location;
    }

    private void createLocatorTaskWithParameters ()
    {
        locatorTask = mapPackage.getLocatorTask();
        geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add( "*" ); //return all parameters
        geocodeParameters.setMaxResults( 1 );   //get closest match
        geocodeParameters.setOutputSpatialReference( mapView.getSpatialReference() );
    }

    private void displayResult ( GeocodeResult geocodeResult, String which )
    {
        setStartMarker( startPoint );
        routeStops.add( new Stop( startPoint ) );

        if ( which.equals( "end" ) )
        {
            endPoint = geocodeResult.getDisplayLocation();
            setEndMarker( endPoint );
            routeStops.add( new Stop( endPoint ) );
        }
        routeParameters.setStops( routeStops );

        if ( startPoint != null && endPoint != null )
        {
            solveForRoute();
        }
    }

    //get the location on the map of the end point
    private void geocodeQuery ( String query, String which )
    {
        System.out.println( query );
        ListenableFuture< List< GeocodeResult > > geocode = locatorTask.geocodeAsync( query, geocodeParameters );

        geocode.addDoneListener( () ->
        {
            try
            {
                List< GeocodeResult > results = geocode.get();

                if ( results.size() > 0 )
                {
                    GeocodeResult result = results.get( 0 );
                    displayResult( result, which );
                }
                else
                {
                    //if street found but house number not found, will return a location in the middle of the street
                    //if street not found, will return a similar named street in the area: OakShire Road -> Oakshire Drive
                    Alert alert = new Alert( Alert.AlertType.INFORMATION, "Address not found" );
                    alert.show();
                }
            }
            catch ( InterruptedException | ExecutionException e )
            {
                e.printStackTrace();
                Alert alert = new Alert( Alert.AlertType.ERROR, "Error getting result" );
                alert.show();
            }
        } );
    }

    public void solveForRoute ()
    {
        System.out.println( "SolveForRoute Function" );

        if ( startPoint != null && endPoint != null )
        {
            try
            {
                RouteResult result = routeTask.solveRouteAsync( routeParameters ).get();

                List< Route > routes = result.getRoutes();
                if ( routes.size() < 1 )
                {
                    System.out.println( "No route" );
                }
                Route route = routes.get( 0 );

                Geometry shape = route.getRouteGeometry();
                routeGraphic = new Graphic( shape, new SimpleLineSymbol( SimpleLineSymbol.Style.SOLID, BLUE_COLOR, 2 ) );
                routeGraphicsOverlay.getGraphics().add( routeGraphic );

                List< DirectionManeuver > directionManeuvers = route.getDirectionManeuvers();

                DirectionManeuver step = directionManeuvers.get( 1 );
                System.out.println( step.getDirectionText() );
                NextTurn.getItems().clear();
                NextTurn.getItems().add( step.getDirectionText() );

                reRoute();

            }
            catch ( InterruptedException | ExecutionException e )
            {
                e.printStackTrace();
                new Alert( Alert.AlertType.ERROR, e.getMessage() + e.getMessage() ).show();
            }
        }
    }

    public void reRoute ()
    {
        if ( atEnd )
        {
            pause();
        }
        Runnable runnable = () ->
        {
            getGPSCurrentPoint();
            startPoint = currentPoint;
            routeStops.clear();
            routeStops.add( new Stop( startPoint ) );
            routeStops.add( new Stop( endPoint ) );

            routeParameters.clearStops();
            routeParameters.setStops( routeStops );

            routeGraphicsOverlay.getGraphics().clear();
            setStartMarker( startPoint );
            setEndMarker( endPoint );

            Platform.runLater( this::solveForRoute );
        };

        ScheduledExecutorService svc = Executors.newSingleThreadScheduledExecutor();
        svc.scheduleWithFixedDelay( runnable, 3, 4, TimeUnit.SECONDS );
    }

    @Override
    public void stop ()
    {
        if ( mapView != null )
        {
            mapView.dispose();
        }
    }

    public void pause ()
    {
        new Alert( Alert.AlertType.INFORMATION, "You have reached your destination." ).showAndWait();
        stop();

        Alert alert = new Alert( Alert.AlertType.CONFIRMATION, "Exit GPS?", ButtonType.YES, ButtonType.NO );
        Optional< ButtonType > result = alert.showAndWait();

        if ( result.get() == ButtonType.YES )
        {
            exit( 0 );
        }
    }
}
