/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.vcell.vis.vismesh.thrift;

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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2016-11-22")
public class ChomboIndexData implements org.apache.thrift.TBase<ChomboIndexData, ChomboIndexData._Fields>, java.io.Serializable, Cloneable, Comparable<ChomboIndexData> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ChomboIndexData");

  private static final org.apache.thrift.protocol.TField DOMAIN_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("domainName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField CHOMBO_SURFACE_INDICES_FIELD_DESC = new org.apache.thrift.protocol.TField("chomboSurfaceIndices", org.apache.thrift.protocol.TType.LIST, (short)2);
  private static final org.apache.thrift.protocol.TField CHOMBO_VOLUME_INDICES_FIELD_DESC = new org.apache.thrift.protocol.TField("chomboVolumeIndices", org.apache.thrift.protocol.TType.LIST, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ChomboIndexDataStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ChomboIndexDataTupleSchemeFactory());
  }

  public String domainName; // required
  public List<ChomboSurfaceIndex> chomboSurfaceIndices; // optional
  public List<ChomboVolumeIndex> chomboVolumeIndices; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    DOMAIN_NAME((short)1, "domainName"),
    CHOMBO_SURFACE_INDICES((short)2, "chomboSurfaceIndices"),
    CHOMBO_VOLUME_INDICES((short)3, "chomboVolumeIndices");

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
        case 1: // DOMAIN_NAME
          return DOMAIN_NAME;
        case 2: // CHOMBO_SURFACE_INDICES
          return CHOMBO_SURFACE_INDICES;
        case 3: // CHOMBO_VOLUME_INDICES
          return CHOMBO_VOLUME_INDICES;
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
  private static final _Fields optionals[] = {_Fields.CHOMBO_SURFACE_INDICES,_Fields.CHOMBO_VOLUME_INDICES};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.DOMAIN_NAME, new org.apache.thrift.meta_data.FieldMetaData("domainName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CHOMBO_SURFACE_INDICES, new org.apache.thrift.meta_data.FieldMetaData("chomboSurfaceIndices", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ChomboSurfaceIndex.class))));
    tmpMap.put(_Fields.CHOMBO_VOLUME_INDICES, new org.apache.thrift.meta_data.FieldMetaData("chomboVolumeIndices", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ChomboVolumeIndex.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ChomboIndexData.class, metaDataMap);
  }

  public ChomboIndexData() {
  }

  public ChomboIndexData(
    String domainName)
  {
    this();
    this.domainName = domainName;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ChomboIndexData(ChomboIndexData other) {
    if (other.isSetDomainName()) {
      this.domainName = other.domainName;
    }
    if (other.isSetChomboSurfaceIndices()) {
      List<ChomboSurfaceIndex> __this__chomboSurfaceIndices = new ArrayList<ChomboSurfaceIndex>(other.chomboSurfaceIndices.size());
      for (ChomboSurfaceIndex other_element : other.chomboSurfaceIndices) {
        __this__chomboSurfaceIndices.add(new ChomboSurfaceIndex(other_element));
      }
      this.chomboSurfaceIndices = __this__chomboSurfaceIndices;
    }
    if (other.isSetChomboVolumeIndices()) {
      List<ChomboVolumeIndex> __this__chomboVolumeIndices = new ArrayList<ChomboVolumeIndex>(other.chomboVolumeIndices.size());
      for (ChomboVolumeIndex other_element : other.chomboVolumeIndices) {
        __this__chomboVolumeIndices.add(new ChomboVolumeIndex(other_element));
      }
      this.chomboVolumeIndices = __this__chomboVolumeIndices;
    }
  }

  public ChomboIndexData deepCopy() {
    return new ChomboIndexData(this);
  }

  @Override
  public void clear() {
    this.domainName = null;
    this.chomboSurfaceIndices = null;
    this.chomboVolumeIndices = null;
  }

  public String getDomainName() {
    return this.domainName;
  }

  public ChomboIndexData setDomainName(String domainName) {
    this.domainName = domainName;
    return this;
  }

  public void unsetDomainName() {
    this.domainName = null;
  }

  /** Returns true if field domainName is set (has been assigned a value) and false otherwise */
  public boolean isSetDomainName() {
    return this.domainName != null;
  }

  public void setDomainNameIsSet(boolean value) {
    if (!value) {
      this.domainName = null;
    }
  }

  public int getChomboSurfaceIndicesSize() {
    return (this.chomboSurfaceIndices == null) ? 0 : this.chomboSurfaceIndices.size();
  }

  public java.util.Iterator<ChomboSurfaceIndex> getChomboSurfaceIndicesIterator() {
    return (this.chomboSurfaceIndices == null) ? null : this.chomboSurfaceIndices.iterator();
  }

  public void addToChomboSurfaceIndices(ChomboSurfaceIndex elem) {
    if (this.chomboSurfaceIndices == null) {
      this.chomboSurfaceIndices = new ArrayList<ChomboSurfaceIndex>();
    }
    this.chomboSurfaceIndices.add(elem);
  }

  public List<ChomboSurfaceIndex> getChomboSurfaceIndices() {
    return this.chomboSurfaceIndices;
  }

  public ChomboIndexData setChomboSurfaceIndices(List<ChomboSurfaceIndex> chomboSurfaceIndices) {
    this.chomboSurfaceIndices = chomboSurfaceIndices;
    return this;
  }

  public void unsetChomboSurfaceIndices() {
    this.chomboSurfaceIndices = null;
  }

  /** Returns true if field chomboSurfaceIndices is set (has been assigned a value) and false otherwise */
  public boolean isSetChomboSurfaceIndices() {
    return this.chomboSurfaceIndices != null;
  }

  public void setChomboSurfaceIndicesIsSet(boolean value) {
    if (!value) {
      this.chomboSurfaceIndices = null;
    }
  }

  public int getChomboVolumeIndicesSize() {
    return (this.chomboVolumeIndices == null) ? 0 : this.chomboVolumeIndices.size();
  }

  public java.util.Iterator<ChomboVolumeIndex> getChomboVolumeIndicesIterator() {
    return (this.chomboVolumeIndices == null) ? null : this.chomboVolumeIndices.iterator();
  }

  public void addToChomboVolumeIndices(ChomboVolumeIndex elem) {
    if (this.chomboVolumeIndices == null) {
      this.chomboVolumeIndices = new ArrayList<ChomboVolumeIndex>();
    }
    this.chomboVolumeIndices.add(elem);
  }

  public List<ChomboVolumeIndex> getChomboVolumeIndices() {
    return this.chomboVolumeIndices;
  }

  public ChomboIndexData setChomboVolumeIndices(List<ChomboVolumeIndex> chomboVolumeIndices) {
    this.chomboVolumeIndices = chomboVolumeIndices;
    return this;
  }

  public void unsetChomboVolumeIndices() {
    this.chomboVolumeIndices = null;
  }

  /** Returns true if field chomboVolumeIndices is set (has been assigned a value) and false otherwise */
  public boolean isSetChomboVolumeIndices() {
    return this.chomboVolumeIndices != null;
  }

  public void setChomboVolumeIndicesIsSet(boolean value) {
    if (!value) {
      this.chomboVolumeIndices = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case DOMAIN_NAME:
      if (value == null) {
        unsetDomainName();
      } else {
        setDomainName((String)value);
      }
      break;

    case CHOMBO_SURFACE_INDICES:
      if (value == null) {
        unsetChomboSurfaceIndices();
      } else {
        setChomboSurfaceIndices((List<ChomboSurfaceIndex>)value);
      }
      break;

    case CHOMBO_VOLUME_INDICES:
      if (value == null) {
        unsetChomboVolumeIndices();
      } else {
        setChomboVolumeIndices((List<ChomboVolumeIndex>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case DOMAIN_NAME:
      return getDomainName();

    case CHOMBO_SURFACE_INDICES:
      return getChomboSurfaceIndices();

    case CHOMBO_VOLUME_INDICES:
      return getChomboVolumeIndices();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case DOMAIN_NAME:
      return isSetDomainName();
    case CHOMBO_SURFACE_INDICES:
      return isSetChomboSurfaceIndices();
    case CHOMBO_VOLUME_INDICES:
      return isSetChomboVolumeIndices();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ChomboIndexData)
      return this.equals((ChomboIndexData)that);
    return false;
  }

  public boolean equals(ChomboIndexData that) {
    if (that == null)
      return false;

    boolean this_present_domainName = true && this.isSetDomainName();
    boolean that_present_domainName = true && that.isSetDomainName();
    if (this_present_domainName || that_present_domainName) {
      if (!(this_present_domainName && that_present_domainName))
        return false;
      if (!this.domainName.equals(that.domainName))
        return false;
    }

    boolean this_present_chomboSurfaceIndices = true && this.isSetChomboSurfaceIndices();
    boolean that_present_chomboSurfaceIndices = true && that.isSetChomboSurfaceIndices();
    if (this_present_chomboSurfaceIndices || that_present_chomboSurfaceIndices) {
      if (!(this_present_chomboSurfaceIndices && that_present_chomboSurfaceIndices))
        return false;
      if (!this.chomboSurfaceIndices.equals(that.chomboSurfaceIndices))
        return false;
    }

    boolean this_present_chomboVolumeIndices = true && this.isSetChomboVolumeIndices();
    boolean that_present_chomboVolumeIndices = true && that.isSetChomboVolumeIndices();
    if (this_present_chomboVolumeIndices || that_present_chomboVolumeIndices) {
      if (!(this_present_chomboVolumeIndices && that_present_chomboVolumeIndices))
        return false;
      if (!this.chomboVolumeIndices.equals(that.chomboVolumeIndices))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_domainName = true && (isSetDomainName());
    list.add(present_domainName);
    if (present_domainName)
      list.add(domainName);

    boolean present_chomboSurfaceIndices = true && (isSetChomboSurfaceIndices());
    list.add(present_chomboSurfaceIndices);
    if (present_chomboSurfaceIndices)
      list.add(chomboSurfaceIndices);

    boolean present_chomboVolumeIndices = true && (isSetChomboVolumeIndices());
    list.add(present_chomboVolumeIndices);
    if (present_chomboVolumeIndices)
      list.add(chomboVolumeIndices);

    return list.hashCode();
  }

  @Override
  public int compareTo(ChomboIndexData other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetDomainName()).compareTo(other.isSetDomainName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDomainName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.domainName, other.domainName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetChomboSurfaceIndices()).compareTo(other.isSetChomboSurfaceIndices());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetChomboSurfaceIndices()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.chomboSurfaceIndices, other.chomboSurfaceIndices);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetChomboVolumeIndices()).compareTo(other.isSetChomboVolumeIndices());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetChomboVolumeIndices()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.chomboVolumeIndices, other.chomboVolumeIndices);
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
    StringBuilder sb = new StringBuilder("ChomboIndexData(");
    boolean first = true;

    sb.append("domainName:");
    if (this.domainName == null) {
      sb.append("null");
    } else {
      sb.append(this.domainName);
    }
    first = false;
    if (isSetChomboSurfaceIndices()) {
      if (!first) sb.append(", ");
      sb.append("chomboSurfaceIndices:");
      if (this.chomboSurfaceIndices == null) {
        sb.append("null");
      } else {
        sb.append(this.chomboSurfaceIndices);
      }
      first = false;
    }
    if (isSetChomboVolumeIndices()) {
      if (!first) sb.append(", ");
      sb.append("chomboVolumeIndices:");
      if (this.chomboVolumeIndices == null) {
        sb.append("null");
      } else {
        sb.append(this.chomboVolumeIndices);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (domainName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'domainName' was not present! Struct: " + toString());
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

  private static class ChomboIndexDataStandardSchemeFactory implements SchemeFactory {
    public ChomboIndexDataStandardScheme getScheme() {
      return new ChomboIndexDataStandardScheme();
    }
  }

  private static class ChomboIndexDataStandardScheme extends StandardScheme<ChomboIndexData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ChomboIndexData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // DOMAIN_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.domainName = iprot.readString();
              struct.setDomainNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CHOMBO_SURFACE_INDICES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list56 = iprot.readListBegin();
                struct.chomboSurfaceIndices = new ArrayList<ChomboSurfaceIndex>(_list56.size);
                ChomboSurfaceIndex _elem57;
                for (int _i58 = 0; _i58 < _list56.size; ++_i58)
                {
                  _elem57 = new ChomboSurfaceIndex();
                  _elem57.read(iprot);
                  struct.chomboSurfaceIndices.add(_elem57);
                }
                iprot.readListEnd();
              }
              struct.setChomboSurfaceIndicesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // CHOMBO_VOLUME_INDICES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list59 = iprot.readListBegin();
                struct.chomboVolumeIndices = new ArrayList<ChomboVolumeIndex>(_list59.size);
                ChomboVolumeIndex _elem60;
                for (int _i61 = 0; _i61 < _list59.size; ++_i61)
                {
                  _elem60 = new ChomboVolumeIndex();
                  _elem60.read(iprot);
                  struct.chomboVolumeIndices.add(_elem60);
                }
                iprot.readListEnd();
              }
              struct.setChomboVolumeIndicesIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ChomboIndexData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.domainName != null) {
        oprot.writeFieldBegin(DOMAIN_NAME_FIELD_DESC);
        oprot.writeString(struct.domainName);
        oprot.writeFieldEnd();
      }
      if (struct.chomboSurfaceIndices != null) {
        if (struct.isSetChomboSurfaceIndices()) {
          oprot.writeFieldBegin(CHOMBO_SURFACE_INDICES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.chomboSurfaceIndices.size()));
            for (ChomboSurfaceIndex _iter62 : struct.chomboSurfaceIndices)
            {
              _iter62.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.chomboVolumeIndices != null) {
        if (struct.isSetChomboVolumeIndices()) {
          oprot.writeFieldBegin(CHOMBO_VOLUME_INDICES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.chomboVolumeIndices.size()));
            for (ChomboVolumeIndex _iter63 : struct.chomboVolumeIndices)
            {
              _iter63.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ChomboIndexDataTupleSchemeFactory implements SchemeFactory {
    public ChomboIndexDataTupleScheme getScheme() {
      return new ChomboIndexDataTupleScheme();
    }
  }

  private static class ChomboIndexDataTupleScheme extends TupleScheme<ChomboIndexData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ChomboIndexData struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.domainName);
      BitSet optionals = new BitSet();
      if (struct.isSetChomboSurfaceIndices()) {
        optionals.set(0);
      }
      if (struct.isSetChomboVolumeIndices()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetChomboSurfaceIndices()) {
        {
          oprot.writeI32(struct.chomboSurfaceIndices.size());
          for (ChomboSurfaceIndex _iter64 : struct.chomboSurfaceIndices)
          {
            _iter64.write(oprot);
          }
        }
      }
      if (struct.isSetChomboVolumeIndices()) {
        {
          oprot.writeI32(struct.chomboVolumeIndices.size());
          for (ChomboVolumeIndex _iter65 : struct.chomboVolumeIndices)
          {
            _iter65.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ChomboIndexData struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.domainName = iprot.readString();
      struct.setDomainNameIsSet(true);
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list66 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.chomboSurfaceIndices = new ArrayList<ChomboSurfaceIndex>(_list66.size);
          ChomboSurfaceIndex _elem67;
          for (int _i68 = 0; _i68 < _list66.size; ++_i68)
          {
            _elem67 = new ChomboSurfaceIndex();
            _elem67.read(iprot);
            struct.chomboSurfaceIndices.add(_elem67);
          }
        }
        struct.setChomboSurfaceIndicesIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list69 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.chomboVolumeIndices = new ArrayList<ChomboVolumeIndex>(_list69.size);
          ChomboVolumeIndex _elem70;
          for (int _i71 = 0; _i71 < _list69.size; ++_i71)
          {
            _elem70 = new ChomboVolumeIndex();
            _elem70.read(iprot);
            struct.chomboVolumeIndices.add(_elem70);
          }
        }
        struct.setChomboVolumeIndicesIsSet(true);
      }
    }
  }

}

