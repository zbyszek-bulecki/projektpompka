import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pageable-table',
  templateUrl: './pageable-table.component.html',
  styleUrls: ['./pageable-table.component.css']
})
export class PageableTableComponent {

  @Input() table: Table | null = null;
  @Input() config: PageableTableConfig = new PageableTableConfig();

  @Output() openPageEvent = new EventEmitter<number>();

  static allowedPageSizes: number[] = [3,5,10,20];

  constructor() { }

  ngOnInit(): void {}

  onChangeSize(){
    this.config.page = 0;
    this.openPage(this.config.page);
  }

  openPage(page: number){
    this.openPageEvent.emit(page);
  }

  getAllowedPageSizes(){
    return PageableTableComponent.allowedPageSizes;
  }

  pageButtons(){
    const totalPages = Math.ceil(this.config.totalElements/this.config.pageSize)-1;
    const range = 3;
    const startPage = Math.max(0, this.config.page - range);
    const endPage = Math.min(totalPages, this.config.page + range);

    return Array.from({ length: endPage - startPage + 1 }, (_, index) => startPage + index);

  }

  static getDefaultPageSize(){
    return PageableTableComponent.allowedPageSizes[0];
  }
}

export class PageableTableConfig{
  page: number = 0;
  totalElements: number = 0;
  pageSize: number = PageableTableComponent.allowedPageSizes[0];
}

export class Table{
  header: TableHeader;
  rows: TableRow[];
  constructor(header: TableHeader, rows: TableRow[]){
    this.header = header;
    this.rows = rows;
  }
}


export class TableHeader{
  columnsHeaders: string[] = []
  constructor(){}
  
  addHeader(columnsHeaders: string){
    this.columnsHeaders.push(columnsHeaders);
  }

  getHeaders(){return this.columnsHeaders;}

  static of(elements: string []){
    let row = new TableHeader();
    for(let element of elements){
      row.addHeader(element);
    }
    return row;
  }
}

export class TableRow{
  columns: TableColumn[] = []
  constructor(){}
  
  addColumn(column: TableColumn){
    this.columns.push(column);
  }

  getColumns(){return this.columns;}

  static of(elements: (string | {value: string, link: string | string[]})[]){
    let row = new TableRow();
    for(let element of elements){
      if(typeof element == 'string'){
        row.addColumn(new TableColumn(element));
      }
      else{
        row.addColumn(new TableColumn(element.value, element.link));
      }
    }
    return row;
  }
}

export class TableColumn{
  value: string | number | null = null;
  link: string | string[] | null = null;

  constructor(value: string | number | null, link?: string | string[] | null){
    this.value = value;
    if(link){
      this.link = link;
    }
  }

  isLinkArray(){
    return Array.isArray(this.link) &&
      this.link.length > 0 &&
      this.link.every((value) => {
        return typeof value === 'string';
      });
  }

  isLinkText(){
    return this.link && !Array.isArray(this.link) &&
      typeof this.link === 'string'; 
  }
}