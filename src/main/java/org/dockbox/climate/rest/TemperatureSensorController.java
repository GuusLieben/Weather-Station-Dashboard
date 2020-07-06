package org.dockbox.climate.rest;

import org.dockbox.climate.IllegalQueryException;
import org.dockbox.climate.model.HumidityMeasurement;
import org.dockbox.climate.model.LightMeasurement;
import org.dockbox.climate.model.PressureMeasurement;
import org.dockbox.climate.model.TemperatureMeasurement;
import org.dockbox.climate.model.mysql.TemperatureRow;
import org.dockbox.climate.repository.TemperatureRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class TemperatureSensorController extends Controller {

    private final TemperatureRepository temperatureRepository;

    public TemperatureSensorController(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    @RequestMapping("/api/light")
    public Object getLightMeasurements(
            @RequestParam(required = false, defaultValue = "-1") int above,
            @RequestParam(required = false, defaultValue = "" + Integer.MAX_VALUE) int below,

            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate before,
            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate after,

            @RequestParam(required = false, defaultValue = "500") int max,
            @RequestParam(required = false, defaultValue = "false") boolean full,
            @RequestParam(required = false, defaultValue = "1") int page
    ) {
        return apply(
                temperatureRepository.findByLightGreaterThanAndLightLessThan(above, below),
                before, after, page, max, full, row -> new LightMeasurement(row.getTimestamp(), row.getLight())
        );
    }

    @RequestMapping("/api/pressure")
    public Object getPressureMeasurements(
            @RequestParam(required = false, defaultValue = "-1.0") double above,
            @RequestParam(required = false, defaultValue = "" + Double.MAX_VALUE) double below,

            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate before,
            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate after,

            @RequestParam(required = false, defaultValue = "500") int max,
            @RequestParam(required = false, defaultValue = "false") boolean full,
            @RequestParam(required = false, defaultValue = "1") int page
    ) {
        return apply(
                temperatureRepository.findByPressureGreaterThanAndPressureLessThan(above, below),
                before, after, page, max, full, row -> new PressureMeasurement(row.getTimestamp(), row.getPressure())
        );
    }

    @RequestMapping("/api/temperature")
    public Object getTemperatureMeasurements(
            @RequestParam(required = false, defaultValue = "-1.0") double t1above,
            @RequestParam(required = false, defaultValue = "-1.0") double t2above,
            @RequestParam(required = false, defaultValue = "" + Double.MAX_VALUE) double t1below,
            @RequestParam(required = false, defaultValue = "" + Double.MAX_VALUE) double t2below,

            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate before,
            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate after,

            @RequestParam(required = false, defaultValue = "500") int max,
            @RequestParam(required = false, defaultValue = "false") boolean full,
            @RequestParam(required = false, defaultValue = "1") int page
    ) {
        return apply(
                temperatureRepository.findByTemp1GreaterThanAndTemp2GreaterThanAndTemp1LessThanAndTemp2LessThan(t1above, t2above, t1below, t2below),
                before, after, page, max, full, row -> new TemperatureMeasurement(row.getTimestamp(), row.getTemp2(), row.getTemp1())
        );
    }

    @RequestMapping("/api/humidity")
    public Object getHumidityMeasurements(
            @RequestParam(required = false, defaultValue = "-1.0") double above,
            @RequestParam(required = false, defaultValue = "" + Double.MAX_VALUE) double below,

            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate before,
            @RequestParam(required = false, defaultValue = "01-01-0000") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate after,

            @RequestParam(required = false, defaultValue = "500") int max,
            @RequestParam(required = false, defaultValue = "false") boolean full,
            @RequestParam(required = false, defaultValue = "1") int page
    ) {
        return apply(
                temperatureRepository.findByHumidityGreaterThanAndHumidityLessThan(above, below),
                before, after, page, max, full, row -> new HumidityMeasurement(row.getTimestamp(), row.getHumidity())
        );
    }

    private static List<?> apply(List<TemperatureRow> list, LocalDate before, LocalDate after, int page, int max, boolean full, Function<? super TemperatureRow, ?> map) {
        checkDates(before, after);
        Stream<TemperatureRow> stream = list.stream().filter(getDatefilter(before, after)).skip((page - 1) * max);
        if (max > 0) stream = stream.limit(max);
        if (full) return stream.collect(Collectors.toList());
        else return stream.map(map).collect(Collectors.toList());
    }

    private static void checkDates(LocalDate before, LocalDate after) {
        LocalDate yearZero = LocalDate.of(0, 1, 1);
        boolean beforeIsSet = !yearZero.isEqual(before);
        boolean afterIsSet = !yearZero.isEqual(after);

        if (beforeIsSet && afterIsSet && !before.equals(after) && !before.isAfter(after)) {
            throw new IllegalQueryException(before, after);
        }
    }

    private static Predicate<? super TemperatureRow> getDatefilter(LocalDate before, LocalDate after) {
        LocalDate yearZero = LocalDate.of(0, 1, 1);
        boolean checkBefore = !yearZero.isEqual(before);
        boolean checkAfter = !yearZero.isEqual(after);

        return row -> {
            LocalDateTime ldt = getLDTFromTemperatureRow(row);
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

    private static LocalDateTime getLDTFromTemperatureRow(TemperatureRow row) {
        Date date = row.getDate();
        Time time = row.getTime();
        LocalDate localDate = date.toLocalDate();
        LocalTime localTime = time.toLocalTime().plus(1, ChronoUnit.HOURS); // Time difference from UTC
        return LocalDateTime.of(localDate, localTime);
    }

}
