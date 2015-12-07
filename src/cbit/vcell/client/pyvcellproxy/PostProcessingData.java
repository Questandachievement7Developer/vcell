/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package cbit.vcell.client.pyvcellproxy;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2015-12-7")
public class PostProcessingData implements org.apache.thrift.TBase<PostProcessingData, PostProcessingData._Fields>, java.io.Serializable, Cloneable, Comparable<PostProcessingData> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PostProcessingData");

  private static final org.apache.thrift.protocol.TField VARIABLE_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("variableList", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField PLOT_DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("plotData", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PostProcessingDataStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PostProcessingDataTupleSchemeFactory());
  }

  public List<VariableInfo> variableList; // required
  public List<PlotData> plotData; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VARIABLE_LIST((short)1, "variableList"),
    PLOT_DATA((short)2, "plotData");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // VARIABLE_LIST
          return VARIABLE_LIST;
        case 2: // PLOT_DATA
          return PLOT_DATA;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VARIABLE_LIST, new org.apache.thrift.meta_data.FieldMetaData("variableList", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.LIST        , "VariableList")));
    tmpMap.put(_Fields.PLOT_DATA, new org.apache.thrift.meta_data.FieldMetaData("plotData", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, PlotData.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PostProcessingData.class, metaDataMap);
  }

  public PostProcessingData() {
  }

  public PostProcessingData(
    List<VariableInfo> variableList,
    List<PlotData> plotData)
  {
    this();
    this.variableList = variableList;
    this.plotData = plotData;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PostProcessingData(PostProcessingData other) {
    if (other.isSetVariableList()) {
      this.variableList = other.variableList;
    }
    if (other.isSetPlotData()) {
      List<PlotData> __this__plotData = new ArrayList<PlotData>(other.plotData.size());
      for (PlotData other_element : other.plotData) {
        __this__plotData.add(new PlotData(other_element));
      }
      this.plotData = __this__plotData;
    }
  }

  public PostProcessingData deepCopy() {
    return new PostProcessingData(this);
  }

  @Override
  public void clear() {
    this.variableList = null;
    this.plotData = null;
  }

  public int getVariableListSize() {
    return (this.variableList == null) ? 0 : this.variableList.size();
  }

  public java.util.Iterator<VariableInfo> getVariableListIterator() {
    return (this.variableList == null) ? null : this.variableList.iterator();
  }

  public void addToVariableList(VariableInfo elem) {
    if (this.variableList == null) {
      this.variableList = new ArrayList<VariableInfo>();
    }
    this.variableList.add(elem);
  }

  public List<VariableInfo> getVariableList() {
    return this.variableList;
  }

  public PostProcessingData setVariableList(List<VariableInfo> variableList) {
    this.variableList = variableList;
    return this;
  }

  public void unsetVariableList() {
    this.variableList = null;
  }

  /** Returns true if field variableList is set (has been assigned a value) and false otherwise */
  public boolean isSetVariableList() {
    return this.variableList != null;
  }

  public void setVariableListIsSet(boolean value) {
    if (!value) {
      this.variableList = null;
    }
  }

  public int getPlotDataSize() {
    return (this.plotData == null) ? 0 : this.plotData.size();
  }

  public java.util.Iterator<PlotData> getPlotDataIterator() {
    return (this.plotData == null) ? null : this.plotData.iterator();
  }

  public void addToPlotData(PlotData elem) {
    if (this.plotData == null) {
      this.plotData = new ArrayList<PlotData>();
    }
    this.plotData.add(elem);
  }

  public List<PlotData> getPlotData() {
    return this.plotData;
  }

  public PostProcessingData setPlotData(List<PlotData> plotData) {
    this.plotData = plotData;
    return this;
  }

  public void unsetPlotData() {
    this.plotData = null;
  }

  /** Returns true if field plotData is set (has been assigned a value) and false otherwise */
  public boolean isSetPlotData() {
    return this.plotData != null;
  }

  public void setPlotDataIsSet(boolean value) {
    if (!value) {
      this.plotData = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case VARIABLE_LIST:
      if (value == null) {
        unsetVariableList();
      } else {
        setVariableList((List<VariableInfo>)value);
      }
      break;

    case PLOT_DATA:
      if (value == null) {
        unsetPlotData();
      } else {
        setPlotData((List<PlotData>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case VARIABLE_LIST:
      return getVariableList();

    case PLOT_DATA:
      return getPlotData();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case VARIABLE_LIST:
      return isSetVariableList();
    case PLOT_DATA:
      return isSetPlotData();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof PostProcessingData)
      return this.equals((PostProcessingData)that);
    return false;
  }

  public boolean equals(PostProcessingData that) {
    if (that == null)
      return false;

    boolean this_present_variableList = true && this.isSetVariableList();
    boolean that_present_variableList = true && that.isSetVariableList();
    if (this_present_variableList || that_present_variableList) {
      if (!(this_present_variableList && that_present_variableList))
        return false;
      if (!this.variableList.equals(that.variableList))
        return false;
    }

    boolean this_present_plotData = true && this.isSetPlotData();
    boolean that_present_plotData = true && that.isSetPlotData();
    if (this_present_plotData || that_present_plotData) {
      if (!(this_present_plotData && that_present_plotData))
        return false;
      if (!this.plotData.equals(that.plotData))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_variableList = true && (isSetVariableList());
    list.add(present_variableList);
    if (present_variableList)
      list.add(variableList);

    boolean present_plotData = true && (isSetPlotData());
    list.add(present_plotData);
    if (present_plotData)
      list.add(plotData);

    return list.hashCode();
  }

  @Override
  public int compareTo(PostProcessingData other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetVariableList()).compareTo(other.isSetVariableList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVariableList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.variableList, other.variableList);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPlotData()).compareTo(other.isSetPlotData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPlotData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.plotData, other.plotData);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("PostProcessingData(");
    boolean first = true;

    sb.append("variableList:");
    if (this.variableList == null) {
      sb.append("null");
    } else {
      sb.append(this.variableList);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("plotData:");
    if (this.plotData == null) {
      sb.append("null");
    } else {
      sb.append(this.plotData);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (variableList == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'variableList' was not present! Struct: " + toString());
    }
    if (plotData == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'plotData' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class PostProcessingDataStandardSchemeFactory implements SchemeFactory {
    public PostProcessingDataStandardScheme getScheme() {
      return new PostProcessingDataStandardScheme();
    }
  }

  private static class PostProcessingDataStandardScheme extends StandardScheme<PostProcessingData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PostProcessingData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VARIABLE_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list16 = iprot.readListBegin();
                struct.variableList = new ArrayList<VariableInfo>(_list16.size);
                VariableInfo _elem17;
                for (int _i18 = 0; _i18 < _list16.size; ++_i18)
                {
                  _elem17 = new VariableInfo();
                  _elem17.read(iprot);
                  struct.variableList.add(_elem17);
                }
                iprot.readListEnd();
              }
              struct.setVariableListIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PLOT_DATA
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list19 = iprot.readListBegin();
                struct.plotData = new ArrayList<PlotData>(_list19.size);
                PlotData _elem20;
                for (int _i21 = 0; _i21 < _list19.size; ++_i21)
                {
                  _elem20 = new PlotData();
                  _elem20.read(iprot);
                  struct.plotData.add(_elem20);
                }
                iprot.readListEnd();
              }
              struct.setPlotDataIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, PostProcessingData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.variableList != null) {
        oprot.writeFieldBegin(VARIABLE_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.variableList.size()));
          for (VariableInfo _iter22 : struct.variableList)
          {
            _iter22.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.plotData != null) {
        oprot.writeFieldBegin(PLOT_DATA_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.plotData.size()));
          for (PlotData _iter23 : struct.plotData)
          {
            _iter23.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PostProcessingDataTupleSchemeFactory implements SchemeFactory {
    public PostProcessingDataTupleScheme getScheme() {
      return new PostProcessingDataTupleScheme();
    }
  }

  private static class PostProcessingDataTupleScheme extends TupleScheme<PostProcessingData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PostProcessingData struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      {
        oprot.writeI32(struct.variableList.size());
        for (VariableInfo _iter24 : struct.variableList)
        {
          _iter24.write(oprot);
        }
      }
      {
        oprot.writeI32(struct.plotData.size());
        for (PlotData _iter25 : struct.plotData)
        {
          _iter25.write(oprot);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PostProcessingData struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list26 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.variableList = new ArrayList<VariableInfo>(_list26.size);
        VariableInfo _elem27;
        for (int _i28 = 0; _i28 < _list26.size; ++_i28)
        {
          _elem27 = new VariableInfo();
          _elem27.read(iprot);
          struct.variableList.add(_elem27);
        }
      }
      struct.setVariableListIsSet(true);
      {
        org.apache.thrift.protocol.TList _list29 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.plotData = new ArrayList<PlotData>(_list29.size);
        PlotData _elem30;
        for (int _i31 = 0; _i31 < _list29.size; ++_i31)
        {
          _elem30 = new PlotData();
          _elem30.read(iprot);
          struct.plotData.add(_elem30);
        }
      }
      struct.setPlotDataIsSet(true);
    }
  }

}

