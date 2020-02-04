To check if coverage rules are met use

<code>mvn clean verify</code>.

---

To see Coberutra report use

<code>mvn clean cobertura:cobertura</code>

Report will be generated in <code>target/site/cobertura/index.html</code>

---
Note that Cobertura does not support some Java 8 features, and for this reason build may contain warnings.