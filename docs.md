---
title: Documentation
nav_title: Documentation
nav_order: 4
nav_collection: docs
---
{% include get_url %}

{% comment %} Show wiki home {% endcomment %}
{% for doc_page in site.docs %}
  {% if doc_page.title == "Home" %}
    {{ doc_page.content }}
    {% break %}
  {% endif %}
{% endfor %}

### List of Wiki files

{% assign categories = site.docs | sort:"nav_order" | group_by: "category" %}

{% if categories.empty or categories.size == 1 and categories[0].name == "" %}
  {% comment %} No categories, show all items {% endcomment %}
<ul>
  {% for doc_page in dropdown_collection.docs %}
    <li><a href="{{ doc_page.url | prepend: baseurl }}">{{ doc_page.title }}</a></li>
  {% endfor %}
</ul>
{% else %}
  {% comment %} Show only items with categories {% endcomment %}
  {% for category in categories %}
    {% if category.name == "" %}
      {% continue %}
    {% endif %}
{{ category.name }}:
<ul>
    {% for doc_page in category.items %}
<li><a href="{{ doc_page.url | prepend: baseurl }}">{{ doc_page.title }}</a></li>
    {% endfor %}
</ul>
  {% endfor %}
{% endif %}
