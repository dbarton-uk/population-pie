package uk.co.i4software.poppie.main;

import org.junit.Test;
import org.primefaces.model.chart.ChartSeries;
import uk.co.i4software.poppie.census.FactName;
import uk.co.i4software.poppie.census.Location;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.co.i4software.poppie.census.FactType.AGES;
import static uk.co.i4software.poppie.census.MockCensus.ABBEY;
import static uk.co.i4software.poppie.census.MockCensus.BATHAVON_NORTH;

/**
 * (c) Copyright i4 Software Ltd. All Rights Reserved.
 *
 * @author David Barton
 * @since June 2015
 */
public class FactModelCreatorTest {

    private static final FactName[] FACT_NAMES = AGES.getFactNames();
    private static final Location[] SINGLE_LOCATION = new Location[]{ABBEY};
    private static final Location[] MULTIPLE_LOCATIONS = new Location[]{ABBEY, BATHAVON_NORTH};
    private static final Number[] SINGLE_LOCATION_PIE_VALUES = {141L, 104L, 102L, 1181L, 2018L, 1148L, 381L, 595L};
    private static final Number[] MULTIPLE_LOCATION_PIE_VALUES = {468L, 104L, 102L, 1181L, 2018L, 3312L, 381L, 595L};

    private static final Number[] ABBEY_TABLE_VALUES = {141L, 104L, 102L, 1181L, 2018L, 1148L, 381L, 595L};
    private static final Number[] ABBEY_TABLE_PERCENTAGES = {2.49, 1.83, 1.80, 20.83, 35.59, 20.25, 6.72, 10.49};

    @Test
    public void testPieChartModelForSingleLocation() {
        testPieChartModel(SINGLE_LOCATION, SINGLE_LOCATION_PIE_VALUES);
    }

    private void testPieChartModel(Location[] locations, Number[] values) {

        final FactModel factModel = factModel(locations);
        final Map<String, Number> pieData = factModel.getPieChartModel().getData();

        for (int i = 0; i < FACT_NAMES.length; i++) {
            assertTrue(pieData.containsKey(FACT_NAMES[i].getDisplayName()));
            assertEquals(values[i], pieData.get(FACT_NAMES[i].getDisplayName()));
        }
    }

    private FactModel factModel(Location[] locations) {
        return new FactModelCreator(locations, FACT_NAMES).create();
    }

    @Test
    public void testPieChartModelForMultipleLocations() {
        testPieChartModel(MULTIPLE_LOCATIONS, MULTIPLE_LOCATION_PIE_VALUES);
    }

    @Test
    public void testTableModel() {

        final FactModel factModel = factModel(new Location[]{ABBEY});

        for (int i = 0; i < FACT_NAMES.length; i++) {
            assertEquals(ABBEY_TABLE_VALUES[i], factModel.valueOf(ABBEY, FACT_NAMES[i]));
            assertEquals(ABBEY_TABLE_PERCENTAGES[i], factModel.percentageOf(ABBEY, FACT_NAMES[i]));
        }
    }

    @Test
    public void testBarChartModel() {
        testBarChartModelForLocations();
    }

    private void testBarChartModelForLocations() {

        final FactModel factModel = factModel(new Location[]{ABBEY});
        final List<ChartSeries> chartSeriesList = factModel.getBarChartModel().getSeries();

        for (int i = 0; i < FACT_NAMES.length; i++) {

            final ChartSeries chartSeries = chartSeriesList.get(i);
            final String factDisplayName = FACT_NAMES[i].getDisplayName();
            final String chartSeriesLabel = chartSeries.getLabel();
            final Number chartValue = chartSeries.getData().get(ABBEY.getLocationName());

            assertEquals(factDisplayName, chartSeriesLabel);
            assertEquals(ABBEY_TABLE_PERCENTAGES[i], chartValue);

        }
    }
}