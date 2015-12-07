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
public class VariableInfo implements org.apache.thrift.TBase<VariableInfo, VariableInfo._Fields>, java.io.Serializable, Cloneable, Comparable<VariableInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("VariableInfo");

  private static final org.apache.thrift.protocol.TField VARIABLE_VTU_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("variableVtuName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField VARIABLE_DISPLAY_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("variableDisplayName", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField DOMAIN_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("domainName", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField VARIABLE_DOMAIN_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("variableDomainType", org.apache.thrift.protocol.TType.I32, (short)4);
  private static final org.apache.thrift.protocol.TField UNITS_LABEL_FIELD_DESC = new org.apache.thrift.protocol.TField("unitsLabel", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField IS_MESH_VAR_FIELD_DESC = new org.apache.thrift.protocol.TField("isMeshVar", org.apache.thrift.protocol.TType.BOOL, (short)6);
  private static final org.apache.thrift.protocol.TField EXPRESSION_STRING_FIELD_DESC = new org.apache.thrift.protocol.TField("expressionString", org.apache.thrift.protocol.TType.STRING, (short)7);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new VariableInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new VariableInfoTupleSchemeFactory());
  }

  public String variableVtuName; // required
  public String variableDisplayName; // required
  public String domainName; // required
  /**
   * 
   * @see DomainType
   */
  public DomainType variableDomainType; // required
  public String unitsLabel; // required
  public boolean isMeshVar; // required
  public String expressionString; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VARIABLE_VTU_NAME((short)1, "variableVtuName"),
    VARIABLE_DISPLAY_NAME((short)2, "variableDisplayName"),
    DOMAIN_NAME((short)3, "domainName"),
    /**
     * 
     * @see DomainType
     */
    VARIABLE_DOMAIN_TYPE((short)4, "variableDomainType"),
    UNITS_LABEL((short)5, "unitsLabel"),
    IS_MESH_VAR((short)6, "isMeshVar"),
    EXPRESSION_STRING((short)7, "expressionString");

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
        case 1: // VARIABLE_VTU_NAME
          return VARIABLE_VTU_NAME;
        case 2: // VARIABLE_DISPLAY_NAME
          return VARIABLE_DISPLAY_NAME;
        case 3: // DOMAIN_NAME
          return DOMAIN_NAME;
        case 4: // VARIABLE_DOMAIN_TYPE
          return VARIABLE_DOMAIN_TYPE;
        case 5: // UNITS_LABEL
          return UNITS_LABEL;
        case 6: // IS_MESH_VAR
          return IS_MESH_VAR;
        case 7: // EXPRESSION_STRING
          return EXPRESSION_STRING;
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
  private static final int __ISMESHVAR_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.EXPRESSION_STRING};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VARIABLE_VTU_NAME, new org.apache.thrift.meta_data.FieldMetaData("variableVtuName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.VARIABLE_DISPLAY_NAME, new org.apache.thrift.meta_data.FieldMetaData("variableDisplayName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.DOMAIN_NAME, new org.apache.thrift.meta_data.FieldMetaData("domainName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , "DomainName")));
    tmpMap.put(_Fields.VARIABLE_DOMAIN_TYPE, new org.apache.thrift.meta_data.FieldMetaData("variableDomainType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, DomainType.class)));
    tmpMap.put(_Fields.UNITS_LABEL, new org.apache.thrift.meta_data.FieldMetaData("unitsLabel", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.IS_MESH_VAR, new org.apache.thrift.meta_data.FieldMetaData("isMeshVar", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.EXPRESSION_STRING, new org.apache.thrift.meta_data.FieldMetaData("expressionString", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(VariableInfo.class, metaDataMap);
  }

  public VariableInfo() {
  }

  public VariableInfo(
    String variableVtuName,
    String variableDisplayName,
    String domainName,
    DomainType variableDomainType,
    String unitsLabel,
    boolean isMeshVar)
  {
    this();
    this.variableVtuName = variableVtuName;
    this.variableDisplayName = variableDisplayName;
    this.domainName = domainName;
    this.variableDomainType = variableDomainType;
    this.unitsLabel = unitsLabel;
    this.isMeshVar = isMeshVar;
    setIsMeshVarIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public VariableInfo(VariableInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetVariableVtuName()) {
      this.variableVtuName = other.variableVtuName;
    }
    if (other.isSetVariableDisplayName()) {
      this.variableDisplayName = other.variableDisplayName;
    }
    if (other.isSetDomainName()) {
      this.domainName = other.domainName;
    }
    if (other.isSetVariableDomainType()) {
      this.variableDomainType = other.variableDomainType;
    }
    if (other.isSetUnitsLabel()) {
      this.unitsLabel = other.unitsLabel;
    }
    this.isMeshVar = other.isMeshVar;
    if (other.isSetExpressionString()) {
      this.expressionString = other.expressionString;
    }
  }

  public VariableInfo deepCopy() {
    return new VariableInfo(this);
  }

  @Override
  public void clear() {
    this.variableVtuName = null;
    this.variableDisplayName = null;
    this.domainName = null;
    this.variableDomainType = null;
    this.unitsLabel = null;
    setIsMeshVarIsSet(false);
    this.isMeshVar = false;
    this.expressionString = null;
  }

  public String getVariableVtuName() {
    return this.variableVtuName;
  }

  public VariableInfo setVariableVtuName(String variableVtuName) {
    this.variableVtuName = variableVtuName;
    return this;
  }

  public void unsetVariableVtuName() {
    this.variableVtuName = null;
  }

  /** Returns true if field variableVtuName is set (has been assigned a value) and false otherwise */
  public boolean isSetVariableVtuName() {
    return this.variableVtuName != null;
  }

  public void setVariableVtuNameIsSet(boolean value) {
    if (!value) {
      this.variableVtuName = null;
    }
  }

  public String getVariableDisplayName() {
    return this.variableDisplayName;
  }

  public VariableInfo setVariableDisplayName(String variableDisplayName) {
    this.variableDisplayName = variableDisplayName;
    return this;
  }

  public void unsetVariableDisplayName() {
    this.variableDisplayName = null;
  }

  /** Returns true if field variableDisplayName is set (has been assigned a value) and false otherwise */
  public boolean isSetVariableDisplayName() {
    return this.variableDisplayName != null;
  }

  public void setVariableDisplayNameIsSet(boolean value) {
    if (!value) {
      this.variableDisplayName = null;
    }
  }

  public String getDomainName() {
    return this.domainName;
  }

  public VariableInfo setDomainName(String domainName) {
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

  /**
   * 
   * @see DomainType
   */
  public DomainType getVariableDomainType() {
    return this.variableDomainType;
  }

  /**
   * 
   * @see DomainType
   */
  public VariableInfo setVariableDomainType(DomainType variableDomainType) {
    this.variableDomainType = variableDomainType;
    return this;
  }

  public void unsetVariableDomainType() {
    this.variableDomainType = null;
  }

  /** Returns true if field variableDomainType is set (has been assigned a value) and false otherwise */
  public boolean isSetVariableDomainType() {
    return this.variableDomainType != null;
  }

  public void setVariableDomainTypeIsSet(boolean value) {
    if (!value) {
      this.variableDomainType = null;
    }
  }

  public String getUnitsLabel() {
    return this.unitsLabel;
  }

  public VariableInfo setUnitsLabel(String unitsLabel) {
    this.unitsLabel = unitsLabel;
    return this;
  }

  public void unsetUnitsLabel() {
    this.unitsLabel = null;
  }

  /** Returns true if field unitsLabel is set (has been assigned a value) and false otherwise */
  public boolean isSetUnitsLabel() {
    return this.unitsLabel != null;
  }

  public void setUnitsLabelIsSet(boolean value) {
    if (!value) {
      this.unitsLabel = null;
    }
  }

  public boolean isIsMeshVar() {
    return this.isMeshVar;
  }

  public VariableInfo setIsMeshVar(boolean isMeshVar) {
    this.isMeshVar = isMeshVar;
    setIsMeshVarIsSet(true);
    return this;
  }

  public void unsetIsMeshVar() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ISMESHVAR_ISSET_ID);
  }

  /** Returns true if field isMeshVar is set (has been assigned a value) and false otherwise */
  public boolean isSetIsMeshVar() {
    return EncodingUtils.testBit(__isset_bitfield, __ISMESHVAR_ISSET_ID);
  }

  public void setIsMeshVarIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ISMESHVAR_ISSET_ID, value);
  }

  public String getExpressionString() {
    return this.expressionString;
  }

  public VariableInfo setExpressionString(String expressionString) {
    this.expressionString = expressionString;
    return this;
  }

  public void unsetExpressionString() {
    this.expressionString = null;
  }

  /** Returns true if field expressionString is set (has been assigned a value) and false otherwise */
  public boolean isSetExpressionString() {
    return this.expressionString != null;
  }

  public void setExpressionStringIsSet(boolean value) {
    if (!value) {
      this.expressionString = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case VARIABLE_VTU_NAME:
      if (value == null) {
        unsetVariableVtuName();
      } else {
        setVariableVtuName((String)value);
      }
      break;

    case VARIABLE_DISPLAY_NAME:
      if (value == null) {
        unsetVariableDisplayName();
      } else {
        setVariableDisplayName((String)value);
      }
      break;

    case DOMAIN_NAME:
      if (value == null) {
        unsetDomainName();
      } else {
        setDomainName((String)value);
      }
      break;

    case VARIABLE_DOMAIN_TYPE:
      if (value == null) {
        unsetVariableDomainType();
      } else {
        setVariableDomainType((DomainType)value);
      }
      break;

    case UNITS_LABEL:
      if (value == null) {
        unsetUnitsLabel();
      } else {
        setUnitsLabel((String)value);
      }
      break;

    case IS_MESH_VAR:
      if (value == null) {
        unsetIsMeshVar();
      } else {
        setIsMeshVar((Boolean)value);
      }
      break;

    case EXPRESSION_STRING:
      if (value == null) {
        unsetExpressionString();
      } else {
        setExpressionString((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case VARIABLE_VTU_NAME:
      return getVariableVtuName();

    case VARIABLE_DISPLAY_NAME:
      return getVariableDisplayName();

    case DOMAIN_NAME:
      return getDomainName();

    case VARIABLE_DOMAIN_TYPE:
      return getVariableDomainType();

    case UNITS_LABEL:
      return getUnitsLabel();

    case IS_MESH_VAR:
      return Boolean.valueOf(isIsMeshVar());

    case EXPRESSION_STRING:
      return getExpressionString();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case VARIABLE_VTU_NAME:
      return isSetVariableVtuName();
    case VARIABLE_DISPLAY_NAME:
      return isSetVariableDisplayName();
    case DOMAIN_NAME:
      return isSetDomainName();
    case VARIABLE_DOMAIN_TYPE:
      return isSetVariableDomainType();
    case UNITS_LABEL:
      return isSetUnitsLabel();
    case IS_MESH_VAR:
      return isSetIsMeshVar();
    case EXPRESSION_STRING:
      return isSetExpressionString();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof VariableInfo)
      return this.equals((VariableInfo)that);
    return false;
  }

  public boolean equals(VariableInfo that) {
    if (that == null)
      return false;

    boolean this_present_variableVtuName = true && this.isSetVariableVtuName();
    boolean that_present_variableVtuName = true && that.isSetVariableVtuName();
    if (this_present_variableVtuName || that_present_variableVtuName) {
      if (!(this_present_variableVtuName && that_present_variableVtuName))
        return false;
      if (!this.variableVtuName.equals(that.variableVtuName))
        return false;
    }

    boolean this_present_variableDisplayName = true && this.isSetVariableDisplayName();
    boolean that_present_variableDisplayName = true && that.isSetVariableDisplayName();
    if (this_present_variableDisplayName || that_present_variableDisplayName) {
      if (!(this_present_variableDisplayName && that_present_variableDisplayName))
        return false;
      if (!this.variableDisplayName.equals(that.variableDisplayName))
        return false;
    }

    boolean this_present_domainName = true && this.isSetDomainName();
    boolean that_present_domainName = true && that.isSetDomainName();
    if (this_present_domainName || that_present_domainName) {
      if (!(this_present_domainName && that_present_domainName))
        return false;
      if (!this.domainName.equals(that.domainName))
        return false;
    }

    boolean this_present_variableDomainType = true && this.isSetVariableDomainType();
    boolean that_present_variableDomainType = true && that.isSetVariableDomainType();
    if (this_present_variableDomainType || that_present_variableDomainType) {
      if (!(this_present_variableDomainType && that_present_variableDomainType))
        return false;
      if (!this.variableDomainType.equals(that.variableDomainType))
        return false;
    }

    boolean this_present_unitsLabel = true && this.isSetUnitsLabel();
    boolean that_present_unitsLabel = true && that.isSetUnitsLabel();
    if (this_present_unitsLabel || that_present_unitsLabel) {
      if (!(this_present_unitsLabel && that_present_unitsLabel))
        return false;
      if (!this.unitsLabel.equals(that.unitsLabel))
        return false;
    }

    boolean this_present_isMeshVar = true;
    boolean that_present_isMeshVar = true;
    if (this_present_isMeshVar || that_present_isMeshVar) {
      if (!(this_present_isMeshVar && that_present_isMeshVar))
        return false;
      if (this.isMeshVar != that.isMeshVar)
        return false;
    }

    boolean this_present_expressionString = true && this.isSetExpressionString();
    boolean that_present_expressionString = true && that.isSetExpressionString();
    if (this_present_expressionString || that_present_expressionString) {
      if (!(this_present_expressionString && that_present_expressionString))
        return false;
      if (!this.expressionString.equals(that.expressionString))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_variableVtuName = true && (isSetVariableVtuName());
    list.add(present_variableVtuName);
    if (present_variableVtuName)
      list.add(variableVtuName);

    boolean present_variableDisplayName = true && (isSetVariableDisplayName());
    list.add(present_variableDisplayName);
    if (present_variableDisplayName)
      list.add(variableDisplayName);

    boolean present_domainName = true && (isSetDomainName());
    list.add(present_domainName);
    if (present_domainName)
      list.add(domainName);

    boolean present_variableDomainType = true && (isSetVariableDomainType());
    list.add(present_variableDomainType);
    if (present_variableDomainType)
      list.add(variableDomainType.getValue());

    boolean present_unitsLabel = true && (isSetUnitsLabel());
    list.add(present_unitsLabel);
    if (present_unitsLabel)
      list.add(unitsLabel);

    boolean present_isMeshVar = true;
    list.add(present_isMeshVar);
    if (present_isMeshVar)
      list.add(isMeshVar);

    boolean present_expressionString = true && (isSetExpressionString());
    list.add(present_expressionString);
    if (present_expressionString)
      list.add(expressionString);

    return list.hashCode();
  }

  @Override
  public int compareTo(VariableInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetVariableVtuName()).compareTo(other.isSetVariableVtuName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVariableVtuName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.variableVtuName, other.variableVtuName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetVariableDisplayName()).compareTo(other.isSetVariableDisplayName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVariableDisplayName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.variableDisplayName, other.variableDisplayName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
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
    lastComparison = Boolean.valueOf(isSetVariableDomainType()).compareTo(other.isSetVariableDomainType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVariableDomainType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.variableDomainType, other.variableDomainType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUnitsLabel()).compareTo(other.isSetUnitsLabel());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUnitsLabel()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.unitsLabel, other.unitsLabel);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetIsMeshVar()).compareTo(other.isSetIsMeshVar());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIsMeshVar()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.isMeshVar, other.isMeshVar);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetExpressionString()).compareTo(other.isSetExpressionString());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetExpressionString()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.expressionString, other.expressionString);
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
    StringBuilder sb = new StringBuilder("VariableInfo(");
    boolean first = true;

    sb.append("variableVtuName:");
    if (this.variableVtuName == null) {
      sb.append("null");
    } else {
      sb.append(this.variableVtuName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("variableDisplayName:");
    if (this.variableDisplayName == null) {
      sb.append("null");
    } else {
      sb.append(this.variableDisplayName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("domainName:");
    if (this.domainName == null) {
      sb.append("null");
    } else {
      sb.append(this.domainName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("variableDomainType:");
    if (this.variableDomainType == null) {
      sb.append("null");
    } else {
      sb.append(this.variableDomainType);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("unitsLabel:");
    if (this.unitsLabel == null) {
      sb.append("null");
    } else {
      sb.append(this.unitsLabel);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("isMeshVar:");
    sb.append(this.isMeshVar);
    first = false;
    if (isSetExpressionString()) {
      if (!first) sb.append(", ");
      sb.append("expressionString:");
      if (this.expressionString == null) {
        sb.append("null");
      } else {
        sb.append(this.expressionString);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (variableVtuName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'variableVtuName' was not present! Struct: " + toString());
    }
    if (variableDisplayName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'variableDisplayName' was not present! Struct: " + toString());
    }
    if (domainName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'domainName' was not present! Struct: " + toString());
    }
    if (variableDomainType == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'variableDomainType' was not present! Struct: " + toString());
    }
    if (unitsLabel == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'unitsLabel' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'isMeshVar' because it's a primitive and you chose the non-beans generator.
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

  private static class VariableInfoStandardSchemeFactory implements SchemeFactory {
    public VariableInfoStandardScheme getScheme() {
      return new VariableInfoStandardScheme();
    }
  }

  private static class VariableInfoStandardScheme extends StandardScheme<VariableInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, VariableInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VARIABLE_VTU_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.variableVtuName = iprot.readString();
              struct.setVariableVtuNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // VARIABLE_DISPLAY_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.variableDisplayName = iprot.readString();
              struct.setVariableDisplayNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // DOMAIN_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.domainName = iprot.readString();
              struct.setDomainNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // VARIABLE_DOMAIN_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.variableDomainType = cbit.vcell.client.pyvcellproxy.DomainType.findByValue(iprot.readI32());
              struct.setVariableDomainTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // UNITS_LABEL
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.unitsLabel = iprot.readString();
              struct.setUnitsLabelIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // IS_MESH_VAR
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.isMeshVar = iprot.readBool();
              struct.setIsMeshVarIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // EXPRESSION_STRING
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.expressionString = iprot.readString();
              struct.setExpressionStringIsSet(true);
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
      if (!struct.isSetIsMeshVar()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'isMeshVar' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, VariableInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.variableVtuName != null) {
        oprot.writeFieldBegin(VARIABLE_VTU_NAME_FIELD_DESC);
        oprot.writeString(struct.variableVtuName);
        oprot.writeFieldEnd();
      }
      if (struct.variableDisplayName != null) {
        oprot.writeFieldBegin(VARIABLE_DISPLAY_NAME_FIELD_DESC);
        oprot.writeString(struct.variableDisplayName);
        oprot.writeFieldEnd();
      }
      if (struct.domainName != null) {
        oprot.writeFieldBegin(DOMAIN_NAME_FIELD_DESC);
        oprot.writeString(struct.domainName);
        oprot.writeFieldEnd();
      }
      if (struct.variableDomainType != null) {
        oprot.writeFieldBegin(VARIABLE_DOMAIN_TYPE_FIELD_DESC);
        oprot.writeI32(struct.variableDomainType.getValue());
        oprot.writeFieldEnd();
      }
      if (struct.unitsLabel != null) {
        oprot.writeFieldBegin(UNITS_LABEL_FIELD_DESC);
        oprot.writeString(struct.unitsLabel);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(IS_MESH_VAR_FIELD_DESC);
      oprot.writeBool(struct.isMeshVar);
      oprot.writeFieldEnd();
      if (struct.expressionString != null) {
        if (struct.isSetExpressionString()) {
          oprot.writeFieldBegin(EXPRESSION_STRING_FIELD_DESC);
          oprot.writeString(struct.expressionString);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class VariableInfoTupleSchemeFactory implements SchemeFactory {
    public VariableInfoTupleScheme getScheme() {
      return new VariableInfoTupleScheme();
    }
  }

  private static class VariableInfoTupleScheme extends TupleScheme<VariableInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, VariableInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.variableVtuName);
      oprot.writeString(struct.variableDisplayName);
      oprot.writeString(struct.domainName);
      oprot.writeI32(struct.variableDomainType.getValue());
      oprot.writeString(struct.unitsLabel);
      oprot.writeBool(struct.isMeshVar);
      BitSet optionals = new BitSet();
      if (struct.isSetExpressionString()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetExpressionString()) {
        oprot.writeString(struct.expressionString);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, VariableInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.variableVtuName = iprot.readString();
      struct.setVariableVtuNameIsSet(true);
      struct.variableDisplayName = iprot.readString();
      struct.setVariableDisplayNameIsSet(true);
      struct.domainName = iprot.readString();
      struct.setDomainNameIsSet(true);
      struct.variableDomainType = cbit.vcell.client.pyvcellproxy.DomainType.findByValue(iprot.readI32());
      struct.setVariableDomainTypeIsSet(true);
      struct.unitsLabel = iprot.readString();
      struct.setUnitsLabelIsSet(true);
      struct.isMeshVar = iprot.readBool();
      struct.setIsMeshVarIsSet(true);
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.expressionString = iprot.readString();
        struct.setExpressionStringIsSet(true);
      }
    }
  }

}

