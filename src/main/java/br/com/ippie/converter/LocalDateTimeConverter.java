package br.com.ippie.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author ayran
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements 
        AttributeConverter<LocalDateTime,Calendar> 
{
    @Override
    public Calendar convertToDatabaseColumn(LocalDateTime now) 
    {
    Calendar c=Calendar.getInstance();
    c.set(now.getYear(),now.getMonthValue(),now.getDayOfMonth(),now.getHour(),
            now.getMinute(),now.getSecond());        
    return c;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Calendar value) 
    {
    return LocalDateTime.ofInstant(value.toInstant(),ZoneId.systemDefault());
    }
}