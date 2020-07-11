package org.dockbox.climate.rest;

import org.dockbox.climate.exceptions.IllegalDateQuery;
import org.dockbox.climate.exceptions.IllegalParameterQuery;
import org.dockbox.climate.model.BothTemperatureMeasurement;
import org.dockbox.climate.model.HumidityMeasurement;
import org.dockbox.climate.model.LightMeasurement;
import org.dockbox.climate.model.Measurement;
import org.dockbox.climate.model.PM10Measurement;
import org.dockbox.climate.model.PressureMeasurement;
import org.dockbox.climate.model.RestResponse;
import org.dockbox.climate.model.SideTemperatureMeasurement;
import org.dockbox.climate.model.mysql.AbstractRow;
import org.dockbox.climate.model.mysql.ParticleRow;
import org.dockbox.climate.model.mysql.TemperatureRow;
import org.dockbox.climate.repository.ParticleRepository;
import org.dockbox.climate.repository.TemperatureRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*")
@RestController
public class SensorController {

    private enum Sort {
        DEFAULT, ASCENDING, DESCENDING;

        boolean isDefault() {
            return this.equals(DEFAULT);
        }

        Boolean getDirectionAsBoolean() {
            switch (this) {
                default:
                case DEFAULT:
                    return null;
                case ASCENDING:
                    return true;
                case DESCENDING:
                    return false;
            }
        }
    }
    private final TemperatureRepository temperatureRepository;
    private final ParticleRepository particleRepository;

    public SensorController(TemperatureRepository temperatureRepository, ParticleRepository particleRepository) {
        this.temperatureRepository = temperatureRepository;
        this.particleRepository = particleRepository;
    }

    @RequestMapping("/api/{type}")
    public RestResponse getLightMeasurements(
            @RequestParam(required = false, defaultValue = "-1") int above,
            @RequestParam(required = false, defaultValue = "" + Integer.MAX_VALUE) int below,

            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate before,
            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate after,

            @RequestParam(required = false, defaultValue = "-1") int max,
            @RequestParam(required = false, defaultValue = "false") boolean full,
            @RequestParam(required = false, defaultValue = "1") int page,

            @RequestParam(required = false, defaultValue = "no") String sort,

            @PathVariable("type") String type
    ) {
        Sort sorter = "asc".equals(sort) ? Sort.ASCENDING : "desc".equals(sort) ? Sort.DESCENDING : Sort.DEFAULT;
        boolean isParticulateMatter = type.startsWith("pm");
        List<?> data = isParticulateMatter
                ? handleParticleTable(above, below, before, after, max > 0 ? max : 200, full, page, type, sorter)
                : handleTemperatureTable(above, below, before, after, max > 0 ? max : 500, full, page, type, sorter);

        RestResponse res;
        if (!full) {
            res = new RestResponse(data);
            Measurement measurement = (Measurement) data.get(0);
            res.addToLegend(measurement);
        } else {
            if (isParticulateMatter) res = new RestResponse(data, RestResponse.FULL_PARTICLE_RES);
            else res = new RestResponse(data, RestResponse.FULL_TEMPERATURE_RES);
        }
        return res;
    }

    // Suppresses potential NPE warning on getDirectionAsBoolean, as this is mitigated by a prepended isDefault condition
    @SuppressWarnings("ConstantConditions")
    private List<?> handleParticleTable(int above, int below, LocalDate before, LocalDate after, int max, boolean full, int page, String type, Sort sort) {
        List<ParticleRow> results;
        Function<ParticleRow, Measurement> conversionFunc;
        Comparator<ParticleRow> sortingComparator = null;

        switch (type) {
            case "pm10":
                results = particleRepository.findByPm10GreaterThanAndPm10LessThan(above, below);
                conversionFunc = row -> new PM10Measurement(row.getTimestamp(), row.getPm10());
                if (!sort.isDefault()) {
                    sortingComparator = (curr, next) -> sort.getDirectionAsBoolean()
                            ? Double.compare(curr.getPm10(), next.getPm10())
                            : Double.compare(next.getPm10(), curr.getPm10());
                }
                break;
            case "pm25":
                results = particleRepository.findByPm25GreaterThanAndPm25LessThan(above, below);
                conversionFunc = row -> new PM10Measurement(row.getTimestamp(), row.getPm25());
                if (!sort.isDefault()) {
                    sortingComparator = (curr, next) -> sort.getDirectionAsBoolean()
                            ? Double.compare(curr.getPm25(), next.getPm25())
                            : Double.compare(next.getPm25(), curr.getPm25());
                }
                break;
            default:
                throw new IllegalParameterQuery("type", type);
        }
        return apply(results, before, after, page, max, full, conversionFunc, sortingComparator);
    }

    // Suppresses potential NPE warning on getDirectionAsBoolean, as this is mitigated by a prepended isDefault condition
    @SuppressWarnings("ConstantConditions")
    private List<?> handleTemperatureTable(int above, int below, LocalDate before, LocalDate after, int max, boolean full, int page, String type, Sort sort) {
        List<TemperatureRow> results;
        Function<TemperatureRow, Measurement> conversionFunc;
        Comparator<TemperatureRow> sortingComparator = null;

        switch (type) {
            case "light":
                results = temperatureRepository.findByLightGreaterThanAndLightLessThan(above, below);
                conversionFunc = row -> new LightMeasurement(row.getTimestamp(), row.getLight());
                if (!sort.isDefault()) {
                    sortingComparator = (curr, next) -> sort.getDirectionAsBoolean()
                            ? Double.compare(curr.getLight(), next.getLight())
                            : Double.compare(next.getLight(), curr.getLight());
                }
                break;
            case "pressure":
                results = temperatureRepository.findByPressureGreaterThanAndPressureLessThan(above, below);
                conversionFunc = row -> new PressureMeasurement(row.getTimestamp(), row.getPressure());
                if (!sort.isDefault()) {
                    sortingComparator = (curr, next) -> sort.getDirectionAsBoolean()
                            ? Double.compare(curr.getPressure(), next.getPressure())
                            : Double.compare(next.getPressure(), curr.getPressure());
                }
                break;
            case "humidity":
                results = temperatureRepository.findByHumidityGreaterThanAndHumidityLessThan(above, below);
                conversionFunc = row -> new HumidityMeasurement(row.getTimestamp(), row.getHumidity());
                if (!sort.isDefault()) {
                    sortingComparator = (curr, next) -> sort.getDirectionAsBoolean()
                            ? Double.compare(curr.getHumidity(), next.getHumidity())
                            : Double.compare(next.getHumidity(), curr.getHumidity());
                }
                break;
            case "outside_temp":
                results = temperatureRepository.findByTemp1GreaterThanAndTemp1LessThan(above, below);
                conversionFunc = row -> new SideTemperatureMeasurement(row.getTimestamp(), row.getTemp1());
                if (!sort.isDefault()) {
                    sortingComparator = (curr, next) -> sort.getDirectionAsBoolean()
                            ? Double.compare(curr.getTemp1(), next.getTemp1())
                            : Double.compare(next.getTemp1(), curr.getTemp1());
                }
                break;
            case "inside_temp":
                results = temperatureRepository.findByTemp2GreaterThanAndTemp2LessThan(above, below);
                conversionFunc = row -> new SideTemperatureMeasurement(row.getTimestamp(), row.getTemp2());
                if (!sort.isDefault()) {
                    sortingComparator = (curr, next) -> sort.getDirectionAsBoolean()
                            ? Double.compare(curr.getTemp2(), next.getTemp2())
                            : Double.compare(next.getTemp2(), curr.getTemp2());
                }
                break;
            case "temperature":
                results = temperatureRepository.findByTemp1GreaterThanAndTemp2GreaterThanAndTemp1LessThanAndTemp2LessThan(above, above, below, below);
                conversionFunc = row -> new BothTemperatureMeasurement(row.getTimestamp(), row.getTemp2(), row.getTemp1());
                if (!sort.isDefault()) {
                    sortingComparator = (curr, next) -> {
                        double currAvg = (curr.getTemp1()+curr.getTemp2())/2;
                        double nextAvg = (next.getTemp1()+next.getTemp2())/2;
                        return (int) (sort.getDirectionAsBoolean()
                                ? Double.compare(currAvg, nextAvg)
                                : Double.compare(nextAvg, currAvg));
                    };
                }
                break;
            default:
                throw new IllegalParameterQuery("type", type);
        }

        return apply(results, before, after, page, max, full, conversionFunc, sortingComparator);
    }

    private static <T extends AbstractRow> List<?> apply(List<T> list, LocalDate before, LocalDate after, int page, int max, boolean full, Function<T, ?> map, Comparator<T> sortingComparator) {
        checkDates(before, after);
        Stream<T> stream = list.stream();

        if (sortingComparator != null) stream = stream.sorted(sortingComparator);

        stream = stream.filter(getDatefilter(before, after)).skip((page - 1) * max);
        if (max > 0) stream = stream.limit(max);

        if (full) return stream.collect(Collectors.toList());
        else return stream.map(map).collect(Collectors.toList());
    }

    private static void checkDates(LocalDate before, LocalDate after) {
        LocalDate yearZero = LocalDate.of(0, 1, 1);
        boolean beforeIsSet = !yearZero.isEqual(before);
        boolean afterIsSet = !yearZero.isEqual(after);

        if (beforeIsSet && afterIsSet && !before.equals(after) && !before.isAfter(after)) {
            throw new IllegalDateQuery(before, after);
        }
    }

    private static <T extends AbstractRow> Predicate<T> getDatefilter(LocalDate before, LocalDate after) {
        LocalDate yearZero = LocalDate.of(0, 1, 1);
        boolean checkBefore = !yearZero.isEqual(before);
        boolean checkAfter = !yearZero.isEqual(after);

        return row -> {
            LocalDateTime ldt = row.getLDT();
            if (checkBefore && checkAfter) {
                // Before is after 01-01-0000, After is after 01-01-0000
                return ldt.isAfter(LocalDateTime.of(after, LocalTime.of(0, 0))) && ldt.isBefore(LocalDateTime.of(before, LocalTime.of(0, 0)));
            } else if (checkAfter) {
                // Before is 01-01-0000, After is after 01-01-0000
                return ldt.isAfter(LocalDateTime.of(after, LocalTime.of(0, 0)));
            } else if (checkBefore) {
                // Before is after 01-01-0000, After is 01-01-0000
                return ldt.isBefore(LocalDateTime.of(before, LocalTime.of(0, 0)));
            } else {
                // Before is 01-01-0000, After is 01-01-0000. Use previous 24 hours
                return ldt.isAfter(LocalDateTime.now().minus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS));
            }
        };
    }
}
