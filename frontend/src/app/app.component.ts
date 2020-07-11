import {Component} from '@angular/core';
import * as axios from 'axios';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  // Application properties
  title = 'frontend';

  // Graph properties
  chartData: any = {};
  colorScheme = {
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5']
  };
  legend: any

  // Default loading object for graphs
  loading = [{
    label: 'Loading..',
    data: [0]
  }]

  // Loaded keys from Axios responses
  keys = []
  // Keys which should not be generated from Axios responses (blacklist)
  hiddenKeys = ['ldt', 'timestamp']

  // Notification is shown at the top of the page, if present
  notification: string

  // TODO: Temporary object
  loadedLight = false

  constructor() {
    console.log('Started at ' + new Date().getTime())
    this.load('light', 'lux', () => {
      // this.unload('lux');
    })
  }

  unload(property) {
    // TODO: Temporary object
    this.loadedLight = false
    delete this.chartData[property]
    let keyI = this.keys.indexOf(property)
    if (keyI >= 0) {
      this.keys.splice(keyI, 1)
    }
  }

  load(url_value, property, after) {
    this.notification = `Loading ${property}...`
    this.chartData[property] = [this.loading]
    axios.default.get(`http://localhost:8080/api/${url_value}?full=true`).then(res => {

      // TODO: Temporary object
      this.loadedLight = true
      this.legend = res.data.legend
      let data = res.data.data

      let resKeys = Object.keys(data[0])
        .filter(key => this.hiddenKeys.indexOf(key) < 0)

      for (let i = 0; i < resKeys.length; i++) {
        let key = resKeys[i];
        this.keys.push(key)

        let serie = {
          name: `${this.legend[key].display_name} (${this.legend[key].symbol})`,
          series: data.map(row => {
            return {
              name: row.timestamp,
              value: row[key]
            }
          })
        }

        this.chartData[key] = [serie];
      }
      this.notification = undefined

      console.log('Finished at ' + new Date().getTime())
      if (after) after()
    }).catch(err => {
      console.log(JSON.stringify(err))
      this.notification = JSON.stringify(err)
    })
  }
}
