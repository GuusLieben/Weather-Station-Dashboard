import {Component, ViewChild} from '@angular/core';
import * as axios from 'axios';
import * as moment from 'moment';
import {DaterangepickerDirective} from "ngx-daterangepicker-material";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  // Application properties
  title = 'frontend';

  // Graph properties
  colorScheme = {
    domain: ['#5AA454', '#2e94bd']
  };
  legend: any = {}

  // Default loading object for graphs
  loading = [{
    label: 'Loading..',
    data: [0]
  }]

  // Loaded keys from Axios responses
  keys = []
  // Keys which should not be generated from Axios responses (blacklist)
  hiddenKeys = ['ldt', 'timestamp']

  // Measurement data
  chartData: any = {};
  minMax: any = {}
  latestMeasurements: any = {}

  // Notification is shown at the top of the page, if present
  notification: string

  // Checkboxes indicate which data is loaded, and allow the user to manually load or unload them
  checkboxes: any = []
  checkboxHistory: any = []

  // Saves all keys currently loaded
  loaded: any = {}

  // Date ranges
  ranges: any = {
    Today: [moment().startOf('day'), moment().endOf('day')],
    Yesterday: [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')],
    'Last 7 Days': [moment().subtract(6, 'days'), moment()],
    'Last 30 Days': [moment().subtract(29, 'days'), moment()],
    'This Month': [moment().startOf('month'), moment().endOf('month')],
    'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
  };

  change(e): void {
    console.log(e);
  }

  constructor() {
    this.load('light', null, true)
  }

  generateCheckbox(property, displayName, urlValue) {
    this.checkboxes.push({
      displayName,
      property,
      urlValue
    })
  }

  calcMin(key) {
    let min = this.minMax[key].min
    let max = this.minMax[key].max
    let diff = max - min
    return min - (diff / 5)
  }

  calcMax(key) {
    let min = this.minMax[key].min
    let max = this.minMax[key].max
    let diff = max - min
    return max + (diff / 5)
  }

  unload(property) {
    this.loaded[property] = false
    delete this.chartData[property]
    delete this.minMax[property]
    delete this.latestMeasurements[property]

    let keyI = this.keys.indexOf(property)
    if (keyI >= 0) this.keys.splice(keyI, 1)
  }

  load(property, after, full = false) {
    this.notification = `Loading ${property}...`
    this.chartData[property] = [this.loading]
    axios.default.get(`http://localhost:8080/api/${property}${full?'?full=true':''}`).then(res => {
      let data = res.data.data
      let resKeys = Object.keys(data[0])
        .filter(key => this.hiddenKeys.indexOf(key) < 0)

      for (let i = 0; i < resKeys.length; i++) {
        let key = resKeys[i];

        this.chartData[key] = [this.loading]
        this.loaded[key] = true
        this.legend[key] = res.data.legend[key]
        this.keys.push(key)

        console.log(key)

        if (this.checkboxHistory.indexOf(key) < 0) {
          this.generateCheckbox(key, this.legend[key].human_readable_name, key)
          this.checkboxHistory.push(key)
        }

        this.minMax[key] = {min: Number.MAX_SAFE_INTEGER, max: Number.MIN_SAFE_INTEGER}

        let serie = {
          name: `${this.legend[key].display_name} (${this.legend[key].symbol})`,
          series: data.map(row => {
            let value = row[key]

            if (value < this.minMax[key].min) this.minMax[key].min = value
            if (value > this.minMax[key].max) this.minMax[key].max = value

            this.latestMeasurements[key] = value;

            return {
              name: row.timestamp,
              value
            }
          })
        }

        this.chartData[key] = [serie];
      }
      this.notification = undefined

      console.log('Finished at ' + new Date().getTime())
      if (after) after()
    }).catch(err => {
      console.error('Oh no!')
      console.error(JSON.stringify(err))
      this.notification = JSON.stringify(err)
    })
  }
}
