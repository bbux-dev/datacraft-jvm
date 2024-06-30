# Module datacraft-jvm
# Package org.datacraft
# Datacraft JVM Usage

Datacraft JDK is a port of the python library [datacraft](https://datacraft.readthedocs.io/en/latest/). It adheres
to the same data and field specification schemas, but provides JDK friendly entry points.

# Examples

## Spec as JSON String

The default format for specifying the structure of the data you want generated is with a DataSpec as a JSON document.
The basic format is a dictionary/map where the key is the top level field name and the value is another dictionary/map
called a FieldSpec that describes the field. For example: `{"id": {"type": "uuid"}}` is a DataSpec for a single field
`id` that is of type `uuid`. This is all that is needed to describe that field. There are lots of other types and
mechanisms for generating data. See the [core types](https://datacraft.readthedocs.io/en/latest/coretypes.html) docs
for more details on types.

### Records as Maps

In the example below we have a Record that consists of three fields: `id`, `date`, and `age`. We create the DataSpec
as a JSON document to specify how each field should be generated. We then pass this to the `Datacraft.entries`
method to get 100 different records as Maps.

```java
import org.datacraft.Datacraft;

import java.util.List;
import java.util.Map;

class TestStuff {
    public static void main(String[] args) {
        String json = """
        {
          "id": {"type": "uuid"},
          "date": {"type": "date.iso" },
          "age": {
            "type": "range",
            "data": [22, 43]
          }
        }
        """;
        List<Map<String, Object>> recordsAsMaps = Datacraft.entries(json, 100);
        System.out.println(recordsAsMaps.get(0));
        //{id=3f0c99c5-6fdb-40f3-926b-67f17963e230, date=2024-06-21T20:28:55, age=22}
    }
}
```

### Records as Objects

Most of the time we want our data as a specific POJO or Java Record. If the field names match the POJO or Record
exactly, you can use the `Datacraft.recordEntries` method:

```java
import org.datacraft.Datacraft;

import java.util.List;

class TestStuff {
    // how we want the data represented
    record Info(String id, String date, Number age) {}

    public static void main(String[] args) {
        String json = """
        {
          "id": {"type": "uuid"},
          "date": {"type": "date.iso" },
          "age": {
            "type": "range",
            "data": [22, 43]
          }
        }
        """;
        List<Info> recordsAsObjects = Datacraft.recordEntries(json, 100, Info.class);
        System.out.println(recordsAsObjects.get(0));
        //Info[id=da459eee-71c9-4b2e-b728-8378eac0b5f2, date=2024-06-27T12:33:22, age=22]
    }
}
```

## Spec with SpecBuilder

If you prefer using code to build your DataSpec you can make use of the `SpecBuilder` class. This will build a
`DataSpec` object which will have similar methods to calling the static Datacraft data generation ones.

```java
import org.datacraft.DataSpec;
import org.datacraft.Datacraft;

import java.util.List;

class TestStuff {
    record Info(String id, String date, Number age) {}

    public static void main(String[] args) {
        DataSpec dataSpec = Datacraft.specBuilder()
                .fieldByKey("id:uuid")
                .fieldByKey("date:date.iso")
                .field("age").range(22, 43)
                .build();
        List<Info> recordsAsMaps = dataSpec.recordEntries( 100, Info.class);
        System.out.println(recordsAsMaps.get(0));
        //Info[id=5b08ae1e-c1d1-4bae-9900-0d51b2b838be, date=2024-06-27T22:53:51, age=22]
    }
}
```