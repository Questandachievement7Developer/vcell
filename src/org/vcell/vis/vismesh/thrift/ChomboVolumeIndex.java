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
public class ChomboVolumeIndex implements org.apache.thrift.TBase<ChomboVolumeIndex, ChomboVolumeIndex._Fields>, java.io.Serializable, Cloneable, Comparable<ChomboVolumeIndex> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ChomboVolumeIndex");

  private static final org.apache.thrift.protocol.TField LEVEL_FIELD_DESC = new org.apache.thrift.protocol.TField("level", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField BOX_NUMBER_FIELD_DESC = new org.apache.thrift.protocol.TField("boxNumber", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField BOX_INDEX_FIELD_DESC = new org.apache.thrift.protocol.TField("boxIndex", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField FRACTION_FIELD_DESC = new org.apache.thrift.protocol.TField("fraction", org.apache.thrift.protocol.TType.DOUBLE, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ChomboVolumeIndexStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ChomboVolumeIndexTupleSchemeFactory());
  }

  public int level; // required
  public int boxNumber; // required
  public int boxIndex; // required
  public double fraction; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LEVEL((short)1, "level"),
    BOX_NUMBER((short)2, "boxNumber"),
    BOX_INDEX((short)3, "boxIndex"),
    FRACTION((short)4, "fraction");

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
        case 1: // LEVEL
          return LEVEL;
        case 2: // BOX_NUMBER
          return BOX_NUMBER;
        case 3: // BOX_INDEX
          return BOX_INDEX;
        case 4: // FRACTION
          return FRACTION;
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
  private static final int __LEVEL_ISSET_ID = 0;
  private static final int __BOXNUMBER_ISSET_ID = 1;
  private static final int __BOXINDEX_ISSET_ID = 2;
  private static final int __FRACTION_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LEVEL, new org.apache.thrift.meta_data.FieldMetaData("level", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.BOX_NUMBER, new org.apache.thrift.meta_data.FieldMetaData("boxNumber", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.BOX_INDEX, new org.apache.thrift.meta_data.FieldMetaData("boxIndex", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.FRACTION, new org.apache.thrift.meta_data.FieldMetaData("fraction", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ChomboVolumeIndex.class, metaDataMap);
  }

  public ChomboVolumeIndex() {
  }

  public ChomboVolumeIndex(
    int level,
    int boxNumber,
    int boxIndex,
    double fraction)
  {
    this();
    this.level = level;
    setLevelIsSet(true);
    this.boxNumber = boxNumber;
    setBoxNumberIsSet(true);
    this.boxIndex = boxIndex;
    setBoxIndexIsSet(true);
    this.fraction = fraction;
    setFractionIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ChomboVolumeIndex(ChomboVolumeIndex other) {
    __isset_bitfield = other.__isset_bitfield;
    this.level = other.level;
    this.boxNumber = other.boxNumber;
    this.boxIndex = other.boxIndex;
    this.fraction = other.fraction;
  }

  public ChomboVolumeIndex deepCopy() {
    return new ChomboVolumeIndex(this);
  }

  @Override
  public void clear() {
    setLevelIsSet(false);
    this.level = 0;
    setBoxNumberIsSet(false);
    this.boxNumber = 0;
    setBoxIndexIsSet(false);
    this.boxIndex = 0;
    setFractionIsSet(false);
    this.fraction = 0.0;
  }

  public int getLevel() {
    return this.level;
  }

  public ChomboVolumeIndex setLevel(int level) {
    this.level = level;
    setLevelIsSet(true);
    return this;
  }

  public void unsetLevel() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __LEVEL_ISSET_ID);
  }

  /** Returns true if field level is set (has been assigned a value) and false otherwise */
  public boolean isSetLevel() {
    return EncodingUtils.testBit(__isset_bitfield, __LEVEL_ISSET_ID);
  }

  public void setLevelIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __LEVEL_ISSET_ID, value);
  }

  public int getBoxNumber() {
    return this.boxNumber;
  }

  public ChomboVolumeIndex setBoxNumber(int boxNumber) {
    this.boxNumber = boxNumber;
    setBoxNumberIsSet(true);
    return this;
  }

  public void unsetBoxNumber() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __BOXNUMBER_ISSET_ID);
  }

  /** Returns true if field boxNumber is set (has been assigned a value) and false otherwise */
  public boolean isSetBoxNumber() {
    return EncodingUtils.testBit(__isset_bitfield, __BOXNUMBER_ISSET_ID);
  }

  public void setBoxNumberIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __BOXNUMBER_ISSET_ID, value);
  }

  public int getBoxIndex() {
    return this.boxIndex;
  }

  public ChomboVolumeIndex setBoxIndex(int boxIndex) {
    this.boxIndex = boxIndex;
    setBoxIndexIsSet(true);
    return this;
  }

  public void unsetBoxIndex() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __BOXINDEX_ISSET_ID);
  }

  /** Returns true if field boxIndex is set (has been assigned a value) and false otherwise */
  public boolean isSetBoxIndex() {
    return EncodingUtils.testBit(__isset_bitfield, __BOXINDEX_ISSET_ID);
  }

  public void setBoxIndexIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __BOXINDEX_ISSET_ID, value);
  }

  public double getFraction() {
    return this.fraction;
  }

  public ChomboVolumeIndex setFraction(double fraction) {
    this.fraction = fraction;
    setFractionIsSet(true);
    return this;
  }

  public void unsetFraction() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __FRACTION_ISSET_ID);
  }

  /** Returns true if field fraction is set (has been assigned a value) and false otherwise */
  public boolean isSetFraction() {
    return EncodingUtils.testBit(__isset_bitfield, __FRACTION_ISSET_ID);
  }

  public void setFractionIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __FRACTION_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case LEVEL:
      if (value == null) {
        unsetLevel();
      } else {
        setLevel((Integer)value);
      }
      break;

    case BOX_NUMBER:
      if (value == null) {
        unsetBoxNumber();
      } else {
        setBoxNumber((Integer)value);
      }
      break;

    case BOX_INDEX:
      if (value == null) {
        unsetBoxIndex();
      } else {
        setBoxIndex((Integer)value);
      }
      break;

    case FRACTION:
      if (value == null) {
        unsetFraction();
      } else {
        setFraction((Double)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case LEVEL:
      return Integer.valueOf(getLevel());

    case BOX_NUMBER:
      return Integer.valueOf(getBoxNumber());

    case BOX_INDEX:
      return Integer.valueOf(getBoxIndex());

    case FRACTION:
      return Double.valueOf(getFraction());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case LEVEL:
      return isSetLevel();
    case BOX_NUMBER:
      return isSetBoxNumber();
    case BOX_INDEX:
      return isSetBoxIndex();
    case FRACTION:
      return isSetFraction();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ChomboVolumeIndex)
      return this.equals((ChomboVolumeIndex)that);
    return false;
  }

  public boolean equals(ChomboVolumeIndex that) {
    if (that == null)
      return false;

    boolean this_present_level = true;
    boolean that_present_level = true;
    if (this_present_level || that_present_level) {
      if (!(this_present_level && that_present_level))
        return false;
      if (this.level != that.level)
        return false;
    }

    boolean this_present_boxNumber = true;
    boolean that_present_boxNumber = true;
    if (this_present_boxNumber || that_present_boxNumber) {
      if (!(this_present_boxNumber && that_present_boxNumber))
        return false;
      if (this.boxNumber != that.boxNumber)
        return false;
    }

    boolean this_present_boxIndex = true;
    boolean that_present_boxIndex = true;
    if (this_present_boxIndex || that_present_boxIndex) {
      if (!(this_present_boxIndex && that_present_boxIndex))
        return false;
      if (this.boxIndex != that.boxIndex)
        return false;
    }

    boolean this_present_fraction = true;
    boolean that_present_fraction = true;
    if (this_present_fraction || that_present_fraction) {
      if (!(this_present_fraction && that_present_fraction))
        return false;
      if (this.fraction != that.fraction)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_level = true;
    list.add(present_level);
    if (present_level)
      list.add(level);

    boolean present_boxNumber = true;
    list.add(present_boxNumber);
    if (present_boxNumber)
      list.add(boxNumber);

    boolean present_boxIndex = true;
    list.add(present_boxIndex);
    if (present_boxIndex)
      list.add(boxIndex);

    boolean present_fraction = true;
    list.add(present_fraction);
    if (present_fraction)
      list.add(fraction);

    return list.hashCode();
  }

  @Override
  public int compareTo(ChomboVolumeIndex other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetLevel()).compareTo(other.isSetLevel());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLevel()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.level, other.level);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBoxNumber()).compareTo(other.isSetBoxNumber());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBoxNumber()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.boxNumber, other.boxNumber);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBoxIndex()).compareTo(other.isSetBoxIndex());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBoxIndex()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.boxIndex, other.boxIndex);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetFraction()).compareTo(other.isSetFraction());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFraction()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.fraction, other.fraction);
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
    StringBuilder sb = new StringBuilder("ChomboVolumeIndex(");
    boolean first = true;

    sb.append("level:");
    sb.append(this.level);
    first = false;
    if (!first) sb.append(", ");
    sb.append("boxNumber:");
    sb.append(this.boxNumber);
    first = false;
    if (!first) sb.append(", ");
    sb.append("boxIndex:");
    sb.append(this.boxIndex);
    first = false;
    if (!first) sb.append(", ");
    sb.append("fraction:");
    sb.append(this.fraction);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'level' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'boxNumber' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'boxIndex' because it's a primitive and you chose the non-beans generator.
    // alas, we cannot check 'fraction' because it's a primitive and you chose the non-beans generator.
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ChomboVolumeIndexStandardSchemeFactory implements SchemeFactory {
    public ChomboVolumeIndexStandardScheme getScheme() {
      return new ChomboVolumeIndexStandardScheme();
    }
  }

  private static class ChomboVolumeIndexStandardScheme extends StandardScheme<ChomboVolumeIndex> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ChomboVolumeIndex struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LEVEL
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.level = iprot.readI32();
              struct.setLevelIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // BOX_NUMBER
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.boxNumber = iprot.readI32();
              struct.setBoxNumberIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // BOX_INDEX
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.boxIndex = iprot.readI32();
              struct.setBoxIndexIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // FRACTION
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.fraction = iprot.readDouble();
              struct.setFractionIsSet(true);
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
      if (!struct.isSetLevel()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'level' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetBoxNumber()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'boxNumber' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetBoxIndex()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'boxIndex' was not found in serialized data! Struct: " + toString());
      }
      if (!struct.isSetFraction()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'fraction' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ChomboVolumeIndex struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(LEVEL_FIELD_DESC);
      oprot.writeI32(struct.level);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(BOX_NUMBER_FIELD_DESC);
      oprot.writeI32(struct.boxNumber);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(BOX_INDEX_FIELD_DESC);
      oprot.writeI32(struct.boxIndex);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(FRACTION_FIELD_DESC);
      oprot.writeDouble(struct.fraction);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ChomboVolumeIndexTupleSchemeFactory implements SchemeFactory {
    public ChomboVolumeIndexTupleScheme getScheme() {
      return new ChomboVolumeIndexTupleScheme();
    }
  }

  private static class ChomboVolumeIndexTupleScheme extends TupleScheme<ChomboVolumeIndex> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ChomboVolumeIndex struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI32(struct.level);
      oprot.writeI32(struct.boxNumber);
      oprot.writeI32(struct.boxIndex);
      oprot.writeDouble(struct.fraction);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ChomboVolumeIndex struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.level = iprot.readI32();
      struct.setLevelIsSet(true);
      struct.boxNumber = iprot.readI32();
      struct.setBoxNumberIsSet(true);
      struct.boxIndex = iprot.readI32();
      struct.setBoxIndexIsSet(true);
      struct.fraction = iprot.readDouble();
      struct.setFractionIsSet(true);
    }
  }

}

