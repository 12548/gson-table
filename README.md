# GsonTable
Save space of JSON array by avoid duplicate column names for [Gson](https://github.com/google/gson).

Before:

```json
[
  {
    "stringField1": "Foo",
    "stringField2": "bar",
    "intField": 4
  },
  {
    "stringField1": "Foo1",
    "stringField2": "bar2",
    "intField": 5
  },
  {
    "stringField1": "Foo4",
    "stringField2": "bar8",
    "intField": 8
  }
]
```

After: 
```json
{
  "__isTable": true,
  "__classOfRow": "org.foo.Bar",
  "cols": ["stringField1", "stringField2", "intField"],
  "rows:": [
    ["Foo", "bar", 4],
    ["Foo1", "bar2", 5],
    ["Foo4", "bar8", 8]
  ]
}
```

WIP
