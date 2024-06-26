{{/*
Expand the name of the chart.
*/}}
{{- define "nessie.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "nessie.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "nessie.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "nessie.labels" -}}
helm.sh/chart: {{ include "nessie.chart" . }}
{{ include "nessie.selectorLabels" . }}
app.kubernetes.io/version: {{ .Chart.Version | quote }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "nessie.selectorLabels" -}}
app.kubernetes.io/name: {{ include "nessie.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "nessie.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "nessie.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Convert a dict into a string formed by a comma-separated list of key-value pairs: key1=value1,key2=value2, ...
*/}}
{{- define "nessie.dictToString" -}}
{{- $list := list -}}
{{- range $k, $v := . -}}
{{- $list = append $list (printf "%s=%s" $k $v) -}}
{{- end -}}
{{ join "," $list }}
{{- end -}}

{{- define "nessie.mergeAdvancedConfig" -}}
{{- $advConfig := index . 0 -}}
{{- $prefix := index . 1 -}}
{{- $dest := index . 2 -}}
{{- range $key, $val := $advConfig -}}
{{- $name := ternary $key (list $prefix $key | join ".") (eq $prefix "") -}}
{{- if kindOf $val | eq "map" -}}
{{- list $val $name $dest | include "nessie.mergeAdvancedConfig" -}}
{{- else -}}
{{- $_ := set $dest $name $val -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Determine the datasource kind based on the jdbcUrl. This relies on the fact that datasource
names should coincide with jdbc schemes in connection URIs.
*/}}
{{- define "nessie.dbKind" -}}
{{- $v := . | split ":" -}}
{{ $v._1 }}
{{- end }}
