import { FASTElement, customElement, html, attr, css, observable, repeat } from "@microsoft/fast-element";

const myTemplate = html<MarketdataComponent>`
  <div class="header">
    <h3>My Marketdata component</h3>
    <ul>
    ${repeat(x => x.instruments, html<string>`
      <li>${x => x}: ${(x,c) => c.parent.getLastPriceRealTime(x)}</li>
    `)}
  </ul>
  </div>
`;

const marketdataComponentCSS = css`
  h4 {
    color: #00ffff;
  }
`;

@customElement({name: "marketdata-component", template: myTemplate, styles: marketdataComponentCSS})
export class MarketdataComponent extends FASTElement {
    @observable lastPrices: number[] = [101.23, 227.12];
    @observable instruments: String[] = ["MSFT", "AAPL"];

    public getLastPriceRealTime(instrument:string) {
        return this.lastPrices[this.instruments.findIndex(i => i === instrument)];
    }
}