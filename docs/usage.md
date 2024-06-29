# Module datacraft-jdk
# Package org.datacraft
# Datacraft JDK Usage

Datacraft JDK is a port of the python library [datacraft](https://datacraft.readthedocs.io/en/latest/). It adheres
to the same data and field specification schemas, but provides JDK friendly entry points.

# Examples

## Spec as JSON String

```java
import org.datacraft.Datacraft;

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
List<Map<String, Object>> records = Datacraft.entries(json, 100);
```